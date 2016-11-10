package space.kyu.punchcard.puchcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.punchcard.code.VerifyCode;
import space.kyu.punchcard.net.ServerOperation;
import space.kyu.punchcard.util.Config;

/**
 * 执行打卡
 * 
 * @author kyu 2016-11-08
 */
public class PunchCard {
	private String verifyCode;
	private String errorMsg;

	public PunchCard() {
		errorMsg = "";
		verifyCode = "";
	}

	/**
	 * 打上午上班卡
	 * 
	 * @return
	 */
	public boolean puchCardAMStart() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("上班登记");
			List<NameValuePair> pairsList = getParamsPairAMStart(params);
			sendPuchCardMsg(pairsList);
			return true;
		} catch (Exception e) {
			errorMsg = e.getMessage();
			return false;
		}
	}

	/**
	 * 打上午下班卡
	 */
	public boolean puchCardAMEnd() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("下班登记");
			List<NameValuePair> pairsList = getParamsPairAMEnd(params);
			sendPuchCardMsg(pairsList);
			return true;
		} catch (Exception e) {
			errorMsg = e.getMessage();
			return false;
		}
	}

	/**
	 * 打下午上班卡
	 * 
	 * @return
	 */
	public boolean puchCardPMStart() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("上班登记");
			List<NameValuePair> pairsList = getParamsPairPMStart(params);
			sendPuchCardMsg(pairsList);
			return true;
		} catch (Exception e) {
			errorMsg = e.getMessage();
			return false;
		}
	}

	/**
	 * 打下午下班卡
	 * 
	 * @return
	 */
	public boolean puchCardPMEnd() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("下班登记");
			List<NameValuePair> pairsList = getParamsPairPMEnd(params);
			sendPuchCardMsg(pairsList);
			return true;
		} catch (Exception e) {
			errorMsg = e.getMessage();
			return false;
		}
	}

	private String getVerifyCode() throws Exception {
		int tryTime = 0;
		do {
			verifyCode = VerifyCode.getCodeIdentityRes();
			tryTime++;
		} while (isVerifyCodeError(verifyCode, tryTime));
		if ("".equals(verifyCode)) {
			throw new Exception(">>>获取验证码失败...");
		}
		return verifyCode;

	}

	private void sendPuchCardMsg(List<NameValuePair> params) throws Exception {
		String result = ServerOperation.sendPuchCardMsg(params);
		if (result != null) {
			if (result.contains("校验码不正确")) {
				throw new Exception(">>>校验码不正确...");
			} else {
				// 打卡成功，将此次获得的验证码存入训练库，并命名为验证码内容
				VerifyCode.storeCode2TrainDir(verifyCode);
			}
		}
	}

	private List<String> analyzePuchCardFunc(String puchCardFunc) {
		int paramStart = puchCardFunc.indexOf("'");
		int paramEnd = puchCardFunc.lastIndexOf("'") + 1;
		String params = puchCardFunc.substring(paramStart, paramEnd).replace("'", "");
		String[] arr = params.split(",");

		List<String> paramList = Arrays.asList(arr);
		return paramList;
	}

	/**
	 * 解析验证码结果是否正确
	 * 
	 * @param verifyCode
	 *            解析的结果
	 * @param tryTime
	 *            已重试次数
	 * @return
	 */
	private boolean isVerifyCodeError(String verifyCode, int tryTime) {
		String regex = "[0-9]{4}";
		Pattern pattern = Pattern.compile(regex);
		boolean matches = pattern.matcher(verifyCode).matches();
		return "".equals(verifyCode) && tryTime < Config.MAX_GET_CODE_TRY_TIME && matches;
	}

	private List<String> getPuchCardParams(String key) throws Exception {
		String html;
		try {
			html = ServerOperation.getTimeCard();
			Document doc = Jsoup.parse(html);
			Elements amStart = doc.select("a:contains(" + key + ")");
			Element element = amStart.get(0);
			String puchCardFunc = element.attr("onclick");
			List<String> params = analyzePuchCardFunc(puchCardFunc);
			return params;
		} catch (Exception e) {
			throw new Exception(">>>获取打卡参数信息失败...");
		}

	}

	private List<NameValuePair> getParamsPairAMStart(List<String> params) {
		// kq4
		// "TimeCard_ServerTime.asp?NumTime=1&num1="+num1+"&num2="+num2+"&num3="+num3+"&num4="+num4+"&CodeStr20090608="+code;
		List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
		NameValuePair pair = null;
		pair = new BasicNameValuePair("NumTime", "1");
		pairsList.add(pair);
		pair = new BasicNameValuePair("num1", params.get(0));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num2", params.get(1));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num3", params.get(2));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num4", params.get(3));
		pairsList.add(pair);
		pair = new BasicNameValuePair("CodeStr20090608", verifyCode);
		pairsList.add(pair);
		return pairsList;
	}

	private List<NameValuePair> getParamsPairPMEnd(List<String> params) {
		// kq4
		// "TimeCard_ServerTime.asp?NumTime=4&k4="+k4+"&SetTime1="+SetTime1+"&num1="+num1+"&num2="+num2+"&num3="+num3+"&num4="+num4+"&CodeStr20090608="+code;
		List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
		NameValuePair pair = null;
		pair = new BasicNameValuePair("NumTime", "4");
		pairsList.add(pair);
		pair = new BasicNameValuePair("k4", params.get(0));
		pairsList.add(pair);
		pair = new BasicNameValuePair("settime1", params.get(1));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num1", params.get(2));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num2", params.get(3));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num3", params.get(4));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num4", params.get(5));
		pairsList.add(pair);
		pair = new BasicNameValuePair("CodeStr20090608", verifyCode);
		pairsList.add(pair);
		return pairsList;
	}

	private List<NameValuePair> getParamsPairPMStart(List<String> params) {
		// kq3
		// "TimeCard_ServerTime.asp?NumTime=3&k4="+k4+"&settime2="+settime2
		// +"&settime3="+settime3+"&num1="+num1+"&num2="+num2+"&num3="+num3+"&num4="+num4+"&CodeStr20090608="+code;
		List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
		NameValuePair pair = null;
		pair = new BasicNameValuePair("NumTime", "3");
		pairsList.add(pair);
		pair = new BasicNameValuePair("k4", params.get(0));
		pairsList.add(pair);
		pair = new BasicNameValuePair("settime2", params.get(1));
		pairsList.add(pair);
		pair = new BasicNameValuePair("settime3", params.get(2));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num1", params.get(3));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num2", params.get(4));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num3", params.get(5));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num4", params.get(6));
		pairsList.add(pair);
		pair = new BasicNameValuePair("CodeStr20090608", verifyCode);
		pairsList.add(pair);
		return pairsList;
	}

	private List<NameValuePair> getParamsPairAMEnd(List<String> params) {
		// kq2
		// "TimeCard_ServerTime.asp?NumTime=2&settime2="+settime2+"&settime3="+settime3+"&num1="+num1+"&num2="+num2+"&num3="+num3+"&num4="+num4+"&CodeStr20090608="+code;
		List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
		NameValuePair pair = null;
		pair = new BasicNameValuePair("NumTime", "2");
		pairsList.add(pair);
		pair = new BasicNameValuePair("settime2", params.get(0));
		pairsList.add(pair);
		pair = new BasicNameValuePair("settime3", params.get(1));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num1", params.get(2));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num2", params.get(3));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num3", params.get(4));
		pairsList.add(pair);
		pair = new BasicNameValuePair("num4", params.get(5));
		pairsList.add(pair);
		pair = new BasicNameValuePair("CodeStr20090608", verifyCode);
		pairsList.add(pair);
		return pairsList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public static void main(String[] args) {
		new PunchCard().puchCardAMStart();
	}
}
