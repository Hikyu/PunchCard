package space.kyu.punchcard.puchcard.state;

import java.util.Date;

public interface State {
	void punchCard(PunchCardContext context);
	Date getPunchCardTime();
}
