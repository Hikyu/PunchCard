package space.kyu.punchcard.puchcard.state;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Config;

public class AMEndState extends AbstractState implements State{

	@Override
	boolean execPunchCard(PunchCard punchCard) {
		return punchCard.puchCardAMEnd();
	}

	@Override
	void changeState(PunchCardContext context) {
		context.changeState(new PMStartState());
	}

	@Override
	String getPunchCardStartTime() {
		return Config.AM_PUCH_CARD_END;
	}

}
