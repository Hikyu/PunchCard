package space.kyu.punchcard.puchcard.state;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Constants;

/**
 * 打上午上班卡
 * @author kyu
 *
 */
public class AMStartState implements State {

	public void punchCard(PunchCardContext context) {
		PunchCard punchCard = new PunchCard();
		boolean success = false;
		int tryNum = 0;
		do {
			success = punchCard.puchCardAMStart();
			tryNum++;
			if (!success) {
				System.err.println(tryNum + ": " + punchCard.getErrorMsg());
			}
		} while (!success && tryNum < Constants.MAX_PUCH_CARD_TRY_TIME);
		System.out.println(">>>上午上班成功打卡...");
		context.changeState(new AMEndState());
	}

}
