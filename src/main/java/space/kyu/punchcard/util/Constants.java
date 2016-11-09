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

import space.kyu.punchcard.net.HttpClientUtil;

/**
 * 常量
 * 
 * @author kyu
 *
 */
public class Constants {
	
	// 获取验证码最大重试次数
	public static final int MAX_GET_CODE_TRY_TIME = 5;
	// 打卡最大重试次数
	public static final int MAX_PUCH_CARD_TRY_TIME = 3;
	// 从打卡起始时间开始 此段间隔内打卡
	public static final int PUCH_CARD_GAP_MINUTES = 10;
	// 上午上班打卡起始时间
	public static final String AM_PUCH_CARD_START = "09:10:00";
	// 上午下班打卡起始时间
	public static final String AM_PUCH_CARD_END = "11:32:00";
	// 下午上班班打卡起始时间
	public static final String PM_PUCH_CARD_START = "11:57:00";
	// 上午下班打卡起始时间
	public static final String PM_PUCH_CARD_END = "18:30:00";


}
