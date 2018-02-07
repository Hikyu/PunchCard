package space.kyu.punchcard;

import java.util.Date;

import org.junit.Test;

import space.kyu.punchcard.puchcard.listener.WeChatListener;

public class SMSTest {

	@Test
	public void testSendSMSMsg() throws InterruptedException {
		String time = "09:12:11";
		String next = "11:32:15";
		String trytime = "5";
		WeChatListener weChatListener = new WeChatListener("D:\\code\\punchcard");
		Thread.sleep(5000);
		weChatListener.done(true, new Date(), 2);
//		assertTrue(SmsMsgSender.sendSmsMsg(time, trytime, next));
	}
}
