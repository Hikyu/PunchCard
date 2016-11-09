package space.kyu.punchcard.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	/**
	 * 获取两个日期间隔内的所有时间 以秒为间隔单位
	 * 
	 * @param beginDate
	 * @param endDate
	 */
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(beginDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		boolean bContinue = true;
		while (bContinue) {
			cal.add(Calendar.SECOND, 1);
			if (endDate.after(cal.getTime())) {
				dateList.add(cal.getTime());
			} else {
				break;
			}
		}
		dateList.add(endDate);
		return dateList;
	}
}
