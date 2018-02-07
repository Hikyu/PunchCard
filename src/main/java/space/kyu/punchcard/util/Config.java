package space.kyu.punchcard.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 常量
 * 
 * @author kyu
 *
 */
public class Config {

	// 获取验证码最大重试次数
	public static int MAX_GET_CODE_TRY_TIME = 5;
	// 打卡最大重试次数
	public static int MAX_PUCH_CARD_TRY_TIME = 10;
	// 从打卡起始时间开始 此段间隔内打卡
	public static int PUCH_CARD_GAP_MINUTES = 5;
	// 上午上班打卡起始时间
	public static String AM_PUCH_CARD_START = "09:10:00";
	// 上午下班打卡起始时间
	public static String AM_PUCH_CARD_END = "11:35:00";
	// 下午上班班打卡起始时间
	public static String PM_PUCH_CARD_START = "12:00:00";
	// 下午下班打卡起始时间
	public static String PM_PUCH_CARD_END = "18:10:00";
	// 账户
	public static String USER = "";
	// 密码
	public static String PASSWORD = "";
	// 手机号
	public static String PHONE = "";
	
	public static String VERIFY_CODE_PATH = "./verifycode/origin/verifyCode.jpg";
	public static String TRAIN_PATH = "./verifycode/train/";
	public static String ORIGIN_PATH = "./verifycode/origin/";

	static {
		Properties prop = new Properties();

		InputStream in = null;
		try {
			in = new ClassPathResource("config.properties").getStream();
			prop.load(in);

			MAX_GET_CODE_TRY_TIME = Integer.valueOf(prop.getProperty("MAX_GET_CODE_TRY_TIME", "5"));
			MAX_PUCH_CARD_TRY_TIME = Integer.valueOf(prop.getProperty("MAX_PUCH_CARD_TRY_TIME", "10"));
			PUCH_CARD_GAP_MINUTES = Integer.valueOf(prop.getProperty("PUCH_CARD_GAP_MINUTES", "5"));
			AM_PUCH_CARD_START = prop.getProperty("AM_PUCH_CARD_START", "09:10:00");
			AM_PUCH_CARD_END = prop.getProperty("AM_PUCH_CARD_END", "11:35:00");
			PM_PUCH_CARD_START = prop.getProperty("PM_PUCH_CARD_START", "12:00:00");
			PM_PUCH_CARD_END = prop.getProperty("PM_PUCH_CARD_END", "18:10:00");
			USER = prop.getProperty("USER", "");
			PASSWORD = prop.getProperty("PWD", "");
			PHONE = prop.getProperty("PHONE", "");
			if ("".equals(USER)) {
				throw new IllegalArgumentException("用户名不合法");
			} 
		} catch (IOException e) {
			//do nothing
			System.err.println("读取配置文件出错");
			throw new ExceptionInInitializerError(e);
		} catch (IllegalArgumentException e) {
			throw new ExceptionInInitializerError(e);
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					//nothing
				}
			}
		}
	}

}
