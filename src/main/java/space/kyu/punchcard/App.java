package space.kyu.punchcard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import space.kyu.punchcard.puchcard.state.PunchCardContext;

/**
 * 
 * @author kyu 
 * 2016-11-09
 */
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
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");    
		System.out.println("下次打卡时间: " + df.format(punchCardTime));  
		System.out.println(PunchCardContext.getInstance(App.this).getCurrentState().getClass().getName());
		timer.schedule(task, punchCardTime);
	}
	
	public static void main(String[] args) {
		App app = new App();
		app.startTask(PunchCardContext.getInstance(app).getPunchCardTime());
	}
}
