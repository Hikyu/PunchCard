package space.kyu.punchcard;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import space.kyu.punchcard.net.SmsMsgSender;

public class SMSTest {

	@Test
	public void testSendSMSMsg() {
		String time = "09:12:11";
		String next = "11:32:15";
		String trytime = "5";
		assertTrue(SmsMsgSender.sendSmsMsg(time, trytime, next));
	}
}
