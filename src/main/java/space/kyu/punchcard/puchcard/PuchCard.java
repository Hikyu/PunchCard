package space.kyu.punchcard.puchcard;

import java.net.URI;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import space.kyu.punchcard.util.Constants;
import space.kyu.punchcard.util.HttpClientUtil;

/**
 * 执行打卡
 * @author kyu
 *
 */
public class PuchCard {
	private String verifyCode;
	
	public void puchAMStartCard() {
		verifyCode = VerifyCode.getCodeIdentityRes();
		String html = getTimeCard();
		Document doc = Jsoup.parse(html); 
		

	}
	
	private String getTimeCard() {
		String content = "";
		try {
			URI uri = new URIBuilder().setScheme(Constants.SCHEME).setHost(Constants.HOST)
					.setPath(Constants.PUCH_CARD_TIME_PATH).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", Constants.User_Agent);
			httpGet.setHeader("Referer", Constants.PUCH_CARD_TIME_REFERER_PATH);
			httpGet.setHeader("Cookie", Constants.getCookie());
			HttpResponse response = HttpClientUtil.doGet(httpGet);
			content = HttpClientUtil.printRespContent(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(date.getTime());
	}
}
