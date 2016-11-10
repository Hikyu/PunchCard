package space.kyu.punchcard.puchcard.state;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Config;

public class PMStartState extends AbstractState implements State {

	@Override
	boolean execPunchCard(PunchCard punchCard) {
		return punchCard.puchCardPMStart();
	}

	@Override
	void changeState(PunchCardContext context) {
		context.changeState(new PMEndState());
	}

	@Override
	String getPunchCardStartTime() {
		return Config.PM_PUCH_CARD_START;
	}

}
