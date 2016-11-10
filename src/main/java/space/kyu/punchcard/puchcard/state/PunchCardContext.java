package space.kyu.punchcard.puchcard.state;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.punchcard.App;
import space.kyu.punchcard.net.ServerOperation;
import space.kyu.punchcard.net.SmsMsgSender;
import space.kyu.punchcard.puchcard.PunchCard;

/**
 *  发起打卡请求 共有四种状态：
 *  应打上午上班卡; 应打上午下班卡; 应打下午上班卡; 应打下午下班卡; 
 *  状态之间自动转换，且状态可扩展 状态模式
 * 
 * @author kyu
 * 2016-11-09
 */
public class PunchCardContext {
	private volatile static PunchCardContext instance;
	private State currentState;
	private App app;
	private PunchCardContext(App app) {
		initCurrentState();
		this.app = app;
	}
	public static PunchCardContext getInstance(App app) {
		if (instance == null) {
			synchronized (PunchCard.class) {
				if (instance == null) {
					instance = new PunchCardContext(app);
				}
			}
		}
		return instance;
	}

	public void punchCard() {
		boolean success = currentState.punchCard(this);
		Date nextTime = currentState.getPunchCardTime();
		sendSmsMsg(success, nextTime);
		//启动下次打卡
		app.startTask(nextTime);
	}

	private void sendSmsMsg(boolean success, Date nextTime) {
		if (!(currentState instanceof AMEndState)) {
			//作一个限制，只在打完上午上班卡时发送短信，节约经费~
			return;
		}
		String time = "fail";
		String trytime = "fail";
		String next = "fail";
		if (success) {
			SimpleDateFormat df=new SimpleDateFormat("hh:mm:ss");
			time = df.format(new Date());
			//暂不支持
			trytime = "-1";
			next = df.format(nextTime);
		}
		boolean sendSmsMsg = SmsMsgSender.sendSmsMsg(time, trytime, next);
		if (sendSmsMsg) {
			System.out.println("短信成功发送~");
		} else {
			System.err.println("短信发送失败...");
		}
	}
	private void initCurrentState() {
		try {
			String html = ServerOperation.getTimeCard();
			Document doc = Jsoup.parse(html);
			Elements elements = doc.select("td.ListCellRow > a[href]");
			Element element = elements.get(0);
			String puchCardFunc = element.attr("onclick");
			if (puchCardFunc.contains("Kq1")) {
				currentState = new AMStartState();
				return;
			}
			
			if (puchCardFunc.contains("Kq2")) {
				currentState = new AMEndState();
				return;
			}
			
			if (puchCardFunc.contains("Kq3")) {
				currentState = new PMStartState();
				return;
			}
			
			if (puchCardFunc.contains("Kq4")) {
				currentState = new PMEndState();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setState(State state) {
		this.currentState = state;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void changeState(State state) {
		setState(state);
	}
	
	public Date getPunchCardTime() {
		return currentState.getPunchCardTime();
	}
}
