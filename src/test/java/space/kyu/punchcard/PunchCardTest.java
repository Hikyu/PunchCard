package space.kyu.punchcard;

import org.junit.Test;

import space.kyu.punchcard.puchcard.state.PunchCardContext;
import space.kyu.punchcard.puchcard.state.State;

public class PunchCardTest {

	@Test
	public void testGetState() {
		PunchCardContext cardContext = PunchCardContext.getInstance(new App());
		State currentState = cardContext.getCurrentState();
		System.out.println(currentState.getClass().getName());
	}
}
