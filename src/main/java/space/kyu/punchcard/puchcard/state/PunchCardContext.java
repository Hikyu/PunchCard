package space.kyu.punchcard.puchcard.state;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.punchcard.App;
import space.kyu.punchcard.net.ServerOperation;
import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.puchcard.listener.PunchCardListener;

/**
 *  发起打卡请求 共有四种状态：
 *  应打上午上班卡; 应打上午下班卡; 应打下午上班卡; 应打下午下班卡; 
 *  状态之间自动转换，且状态可扩展 状态模式
 * 
 * @author kyu
 */
public class PunchCardContext {
	private volatile static PunchCardContext instance;
	private State currentState;
	private App app;
	private List<PunchCardListener> listeners;
	private PunchCardContext(App app) {
		listeners = new ArrayList<>();
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
		done(success, nextTime);
		//启动下次打卡
		app.startTask(nextTime);
	}

	private void done(boolean success, Date nextTime) {
		for (PunchCardListener punchCardListener : listeners) {
			punchCardListener.done(success, nextTime, -1);
		}
	}
	private void initCurrentState() {
		try {
			String html = ServerOperation.getTimeCard();
			Document doc = Jsoup.parse(html);
			Elements elements = doc.select("td.ListCellRow > a[href]");
			Element element = elements.get(0);
			String puchCardFunc = element.attr("onclick");
			
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
			//如果不是上午下班，下午上班，下午下班，则默认打上午上班卡
			currentState = new AMStartState();
			
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
	
	public void registerListener(PunchCardListener listener) {
		listeners.add(listener);
	}
}
