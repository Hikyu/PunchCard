package space.kyu.punchcard.puchcard.state;

import java.util.Date;

public interface State {
	boolean punchCard(PunchCardContext context);
	Date getPunchCardTime();
}
