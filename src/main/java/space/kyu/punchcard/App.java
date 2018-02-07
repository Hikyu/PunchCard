package space.kyu.punchcard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import space.kyu.punchcard.puchcard.listener.PunchCardListener;
import space.kyu.punchcard.puchcard.listener.SmsMsgListener;
import space.kyu.punchcard.puchcard.listener.WeChatListener;
import space.kyu.punchcard.puchcard.state.PunchCardContext;
import space.kyu.punchcard.util.Config;

public class App {
	private static Timer timer;
	static {
		timer = new Timer(false);
	}

	class PunchCardTask extends TimerTask {
		@Override
		public void run() {
			PunchCardContext instance = PunchCardContext.getInstance(App.this);
			instance.punchCard();
		}
	}

	public void startTask(Date punchCardTime) {
		PunchCardTask task = new PunchCardTask();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(">>>>下次打卡时间: " + df.format(punchCardTime));
		System.out.println(PunchCardContext.getInstance(App.this).getCurrentState().getClass().getName());
		timer.schedule(task, punchCardTime);
	}

	public static void main(String[] args) {
		System.out.println("-----------------------------------");
		System.out.println("用户: " + Config.USER);
		System.out.println("密码: " + Config.PASSWORD);
		System.out.println("手机号: " + Config.PHONE);
		System.out.println("获取验证码最大重试次数: " + Config.MAX_GET_CODE_TRY_TIME);
		System.out.println("打卡最大重试次数: " + Config.MAX_PUCH_CARD_TRY_TIME);
		System.out.println("从打卡起始时间开始 此段间隔内打卡: " + Config.PUCH_CARD_GAP_MINUTES);
		System.out.println("上午上班打卡起始时间: " + Config.AM_PUCH_CARD_START);
		System.out.println("上午下班打卡起始时间: " + Config.AM_PUCH_CARD_END);
		System.out.println("下午上班打卡起始时间: " + Config.PM_PUCH_CARD_START);
		System.out.println("下午下班打卡起始时间: " + Config.PM_PUCH_CARD_END);
		System.out.println("-----------------------------------");
		App app = new App();
//		PunchCardListener smsListener = new SmsMsgListener();
		WeChatListener weChatListener = new WeChatListener(System.getProperty("user.dir"));
		PunchCardContext.getInstance(app).registerListener(weChatListener);
		app.startTask(PunchCardContext.getInstance(app).getPunchCardTime());
	}

}
