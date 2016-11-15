package space.kyu.punchcard.util;

/**
 * 常量
 * 
 * @author kyu
 *
 */
public class Config {
	
	// 获取验证码最大重试次数
	public static final int MAX_GET_CODE_TRY_TIME = 5;
	// 打卡最大重试次数
	public static final int MAX_PUCH_CARD_TRY_TIME = 10;
	// 从打卡起始时间开始 此段间隔内打卡
	public static final int PUCH_CARD_GAP_MINUTES = 5;
	// 上午上班打卡起始时间
	public static final String AM_PUCH_CARD_START = "09:10:00";
	// 上午下班打卡起始时间
	public static final String AM_PUCH_CARD_END = "11:35:00";
	// 下午上班班打卡起始时间
	public static final String PM_PUCH_CARD_START = "12:00:00";
	// 下午下班打卡起始时间
	public static final String PM_PUCH_CARD_END = "18:00:00";


}
