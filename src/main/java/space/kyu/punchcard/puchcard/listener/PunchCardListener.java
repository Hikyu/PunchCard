package space.kyu.punchcard.puchcard.listener;

import java.util.Date;

public interface PunchCardListener {
	void done(boolean success, Date nextTime, int tryTime);
}
