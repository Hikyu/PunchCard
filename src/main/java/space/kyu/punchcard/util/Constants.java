package space.kyu.punchcard.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
/**
 * 常量
 * @author kyu
 *
 */
public class Constants {
	private static String COOKIE = "";
	public static final String USER = "yukai";
	public static final String PASSWORD = "yukai";
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
	public static int PIC_NO = 0;
	
	//获取验证码最大重试次数
	public static final int MAX_GET_CODE_TRY_TIME = 5;
	//打卡最大重试次数
	public static final int MAX_PUCH_CARD_TRY_TIME = 3;

	public static int getPicNo() {
		return ++PIC_NO;
	}

	public static String getCookie() {
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
