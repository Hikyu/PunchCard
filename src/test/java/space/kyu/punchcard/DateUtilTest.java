package space.kyu.punchcard;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import space.kyu.punchcard.puchcard.state.AMEndState;
import space.kyu.punchcard.puchcard.state.AMStartState;
import space.kyu.punchcard.util.Config;
import space.kyu.punchcard.util.DateUtil;

public class DateUtilTest {
	@Test
	public void testDatesBetween2Date() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date1 = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 1);
		Date dat2 = calendar.getTime();
		
		List<Date> dates = DateUtil.getDatesBetweenTwoDate(date1, dat2);
		assertEquals(62, dates.size());
	}
	
	@Test
	public void testDateFormat() {
		try {
			Map<String, String>map = DateUtil.getHMSFromFormatDate(Config.AM_PUCH_CARD_START);
			System.out.println(Integer.valueOf(map.get("h")));
			System.out.println(Integer.valueOf(map.get("m")));
			System.out.println(Integer.valueOf(map.get("s")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPunchCardTime() {
		Date date = new AMStartState().getPunchCardTime();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");    
		System.out.println(df.format(date));  
		
		Date date1 = new AMEndState().getPunchCardTime();
		System.out.println(df.format(date1));  
	}
}
