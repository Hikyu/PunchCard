package space.kyu.punchcard.puchcard.state;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.punchcard.net.ServerOperation;
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

	private PunchCardContext() {
		initCurrentState();
	}
	public static PunchCardContext getInstance() {
		if (instance == null) {
			synchronized (PunchCard.class) {
				if (instance == null) {
					instance = new PunchCardContext();
				}
			}
		}
		return instance;
	}

	public void punchCard() {
		currentState.punchCard(this);
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
}
