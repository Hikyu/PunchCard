package space.kyu.punchcard;

import java.util.Timer;
import java.util.TimerTask;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.puchcard.state.PunchCardContext;

/**
 * 
 * @author kyu 
 * 2016-11-09
 */
public class App {
	private static Timer timer;
	static {
		timer = new Timer(true);
	}
	public static void main(String[] args) {
	}
	class PunchCardTask extends TimerTask {
		@Override
		public void run() {
			PunchCardContext instance = PunchCardContext.getInstance();
			instance.punchCard();
		}
	}
	
}
