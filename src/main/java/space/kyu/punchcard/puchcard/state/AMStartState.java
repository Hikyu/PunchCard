package space.kyu.punchcard.puchcard.state;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Config;

/**
 * 打上午上班卡
 * 
 * @author kyu
 *
 */
public class AMStartState extends AbstractState implements State {

	@Override
	boolean execPunchCard(PunchCard punchCard) {
		return punchCard.puchCardAMStart();
	}

	@Override
	void changeState(PunchCardContext context) {
		context.changeState(new AMEndState());
	}

	@Override
	String getPunchCardStartTime() {
		return Config.AM_PUCH_CARD_START;
	}

}
