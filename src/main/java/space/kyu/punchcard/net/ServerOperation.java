package space.kyu.punchcard.net;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import space.kyu.punchcard.util.Config;
/**
 * 与服务器交互工具类
 * @author kyu
 *
 */
public class ServerOperation {
	private static int BUFFER_SIZE = 1 * 1024 * 1024;
	private static String COOKIE = "";
	public static final String USER = Config.USER;
	public static final String PASSWORD = Config.PASSWORD;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";
	public static final String SCHEME = "http";
	public static final String HOST = "192.168.101.25";
	public static final String LOGIN_REFERER_PATH = "http://192.168.101.25/System/System_login.asp?screen=4";
	public static final String LOGIN_VERIFY_PATH = "/System/system_login_verify.asp";
	public static final String VEARIFY_CODE_PATH = "/TimeCard/Kq_GetCode.asp";
	public static final String VEARIFY_CODE_REFERER_PATH = "http://192.168.101.25/TimeCard/TimeCard_Kq.asp";
	public static final String PUCH_CARD_TIME_PATH = "/TimeCard/TimeCard_Kq.asp";
	public static final String PUCH_CARD_TIME_REFERER_PATH = "http://192.168.101.25/System/DetailFrame.asp?WorkFrame=%2FTimeCard%2FTimeCard%5FKq%2Easp&TitleName=%BF%BC%C7%DA%B9%DC%C0%ED%2D%3E%BF%BC%C7%DA%B5%C7%BC%C7";
	public static final String PUCH_CARD_PATH = "/TimeCard/TimeCard_ServerTime.asp";
	public static final String PUCH_CARD_REFERER_PATH = "http://192.168.101.25/TimeCard/TimeCard_Kq.asp";


	public static String getTimeCard() throws Exception {
		String content = "";
		try {
			URI uri = new URIBuilder().setScheme(SCHEME).setHost(HOST)
					.setPath(PUCH_CARD_TIME_PATH).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", USER_AGENT);
			httpGet.setHeader("Referer", PUCH_CARD_TIME_REFERER_PATH);
			httpGet.setHeader("Cookie", getCookie());
			HttpResponse response = HttpClientUtil.doGet(httpGet);
			content = HttpClientUtil.printRespContent(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return content;
	}

	public static String sendPuchCardMsg(List<NameValuePair> params) throws Exception {
		String result = null;
		try {
			URI uri = new URIBuilder().setScheme(SCHEME).setHost(HOST)
					.setPath(PUCH_CARD_PATH).setParameters(params).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", USER_AGENT);
			httpGet.setHeader("Cookie", getCookie());
			httpGet.setHeader("Referer", PUCH_CARD_REFERER_PATH);
			HttpResponse resp = HttpClientUtil.doGet(httpGet);
			result = HttpClientUtil.printRespContent(resp);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(">>>发送打卡请求失败...");
		}

		return result;
	}

	public static byte[] getVerifyCode() throws Exception {
		URI uri = new URIBuilder().setScheme(SCHEME).setHost(HOST)
				.setPath(VEARIFY_CODE_PATH).build();

		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("User-Agent", USER_AGENT);
		httpGet.setHeader("Referer", VEARIFY_CODE_REFERER_PATH);
		httpGet.setHeader("Cookie", getCookie());
		HttpResponse resp = HttpClientUtil.doGet(httpGet);
		return getCodeImg(resp);
	}

	private static byte[] getCodeImg(HttpResponse response) throws Exception {
		byte[] img = null;
		InputStream content = null;
		try {
			content = HttpClientUtil.getStreamFomeResp(response);
			byte[] buffer = new byte[BUFFER_SIZE];
			int size = 0;
			int totalLength = 0;
			do {
				size = content.read(buffer, 0, BUFFER_SIZE);
				if (size == -1) {
					break;
				}
				if (size < BUFFER_SIZE) {
					img = new byte[totalLength + size];
					System.arraycopy(buffer, 0, img, totalLength, size);
					totalLength += size;
				} else if (size == BUFFER_SIZE) {
					img = new byte[totalLength + BUFFER_SIZE];
					System.arraycopy(buffer, 0, img, totalLength, BUFFER_SIZE);
					totalLength += BUFFER_SIZE;
				}
			} while (size != -1);
		} finally {
			if (content != null) {
				content.close();
			}
		}
		return img;
	}
	
	private static String getCookie() {
		if (COOKIE == null || "".equals(COOKIE)) {
			try {
				COOKIE = LoginUtil.login();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return COOKIE;
	}

	static class LoginUtil {
		/**
		 * 获取登录cookie
		 * 
		 * @return
		 * @throws Exception
		 */
		public static String login() throws Exception {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("username", USER));
			formparams.add(new BasicNameValuePair("password", PASSWORD));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

			URI uri = new URIBuilder().setScheme(SCHEME).setHost(HOST).setPath(LOGIN_VERIFY_PATH).build();
			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("User-Agent", USER_AGENT);
			httppost.setHeader("Referer", LOGIN_REFERER_PATH);
			httppost.setEntity(entity);

			return getCookieFromResp(HttpClientUtil.doPost(httppost));
		}

		private static String getCookieFromResp(HttpResponse response) {
			String oriCookie = null;
			Header[] cookies = response.getHeaders("Set-Cookie");
			for (int i = 0; i < cookies.length; i++) {
				Header cookie = cookies[i];
				if (cookie != null) {
					oriCookie += cookie;
				}
			}

			return getRealCookie(oriCookie);
		}

		private static String getRealCookie(String oriCookie) {
			String cookie = oriCookie;
			String cookie1 = cookie.substring(cookie.indexOf("UserInfo"), cookie.indexOf(";") + 1);
			String cookie2 = cookie.substring(cookie.indexOf("ASPSESSIONID"), cookie.lastIndexOf(";"));
			return "TZ=0;" + cookie1 + cookie2;
		}

	}

}
