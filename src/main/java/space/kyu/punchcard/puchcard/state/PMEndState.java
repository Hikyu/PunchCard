package space.kyu.punchcard.puchcard.state;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Config;

public class PMEndState extends AbstractState implements State {

	@Override
	boolean execPunchCard(PunchCard punchCard) {
		return punchCard.puchCardPMEnd();
	}

	@Override
	void changeState(PunchCardContext context) {
		context.changeState(new AMStartState());
	}

	@Override
	String getPunchCardStartTime() {
		return Config.PM_PUCH_CARD_END;
	}

}
