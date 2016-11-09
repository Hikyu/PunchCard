package space.kyu.punchcard.puchcard;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.punchcard.util.Constants;
import space.kyu.punchcard.util.HttpClientUtil;

/**
 * 执行打卡
 * 
 * @author kyu
 *
 */
public class PunchCard {
	private String verifyCode;

	/**
	 * 打上午上班卡
	 */
	public void puchCardAMStart() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("上班登记");
			List<NameValuePair> pairsList = getParamsPairAMStart(params);
			sendPuchCardMsg(pairsList);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	/**
	 * 打上午下班卡
	 */
	public void puchCardAMEnd() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("下班登记");
			List<NameValuePair> pairsList = getParamsPairAMEnd(params);
			sendPuchCardMsg(pairsList);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
	}

	/**
	 * 打下午上班卡
	 */
	public void puchCardPMStart() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("上班登记");
			List<NameValuePair> pairsList = getParamsPairPMStart(params);
			sendPuchCardMsg(pairsList);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	/**
	 * 打下午下班卡
	 */
	public void puchCardPMEnd() {
		try {
			getVerifyCode();
			List<String> params = getPuchCardParams("下班登记");
			List<NameValuePair> pairsList = getParamsPairPMEnd(params);
			sendPuchCardMsg(pairsList);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
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
		return "".equals(verifyCode) && tryTime < Constants.MAX_GET_CODE_TRY_TIME && matches;
	}

	private List<String> getPuchCardParams(String key) throws Exception {
		String html;
		try {
			html = getTimeCard();
		} catch (Exception e) {
			throw new Exception(">>>获取打卡参数信息失败...");
		}
		Document doc = Jsoup.parse(html);
		Elements amStart = doc.select("a:contains(" + key + ")");
		Element element = amStart.get(0);
		String puchCardFunc = element.attr("onclick");
		List<String> params = analyzePuchCardFunc(puchCardFunc);
		return params;
	}
	
	private List<NameValuePair> getParamsPairAMStart(List<String> params) {
		// kq4
		// ="TimeCard_ServerTime.asp?NumTime=1&num1="+num1+"&num2="+num2+"&num3="+num3+"&num4="+num4+"&CodeStr20090608="+code;
		List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
		NameValuePair pair = null;
		pair = new BasicNameValuePair("NumTime", "1");
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

	private void sendPuchCardMsg(List<NameValuePair> params) throws Exception {
		try {
			URI uri = new URIBuilder().setScheme(Constants.SCHEME).setHost(Constants.HOST)
					.setPath(Constants.PUCH_CARD_PATH).setParameters(params).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", Constants.USER_AGENT);
			httpGet.setHeader("Cookie", Constants.getCookie());
			httpGet.setHeader("Referer", Constants.PUCH_CARD_REFERER_PATH);
			HttpResponse resp = HttpClientUtil.doGet(httpGet);
			String content = HttpClientUtil.printRespContent(resp);
			
			System.out.println(content);
			//TODO 如打卡成功，将此次获得的验证码存入训练库，并命名为验证码内容
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(">>>发送打卡请求失败...");
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

	private String getTimeCard() throws Exception {
		String content = "";
		try {
			URI uri = new URIBuilder().setScheme(Constants.SCHEME).setHost(Constants.HOST)
					.setPath(Constants.PUCH_CARD_TIME_PATH).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", Constants.USER_AGENT);
			httpGet.setHeader("Referer", Constants.PUCH_CARD_TIME_REFERER_PATH);
			httpGet.setHeader("Cookie", Constants.getCookie());
			HttpResponse response = HttpClientUtil.doGet(httpGet);
			content = HttpClientUtil.printRespContent(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return content;
	}

	public static void main(String[] args) {
		new PunchCard().puchCardPMStart();
	}
}
