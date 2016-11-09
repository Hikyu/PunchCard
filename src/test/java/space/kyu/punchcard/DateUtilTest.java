package space.kyu.punchcard;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

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
}
