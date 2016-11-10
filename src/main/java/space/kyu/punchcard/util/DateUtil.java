package space.kyu.punchcard.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
	
	public static boolean date2AfterDate1(Date date1, Date date2) {
		return date2.after(date1);
	}
	
	/**
	 * 获取 hh:mm:ss 格式的日期的时分秒信息
	 * @param formatDate
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> getHMSFromFormatDate(String formatDate) throws Exception {
		String regex = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5][0-9]))$";
		Pattern pattern = Pattern.compile(regex);
		boolean matches = pattern.matcher(formatDate).matches();
		if (!matches) {
			throw new Exception("Error date format");
		}
		HashMap<String, String> map = new HashMap<String, String>(3);
		String[] hms = formatDate.split(":");
		map.put("h", hms[0]);
		map.put("m", hms[1]);
		map.put("s", hms[2]);
		return map;
	}
}
