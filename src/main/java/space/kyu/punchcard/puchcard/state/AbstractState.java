package space.kyu.punchcard.puchcard.state;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import space.kyu.punchcard.puchcard.PunchCard;
import space.kyu.punchcard.util.Config;
import space.kyu.punchcard.util.DateUtil;

public abstract class AbstractState implements State {

	public boolean punchCard(PunchCardContext context) {
		PunchCard punchCard = new PunchCard();
		boolean success = false;
		int tryNum = 0;
		do {
			success = execPunchCard(punchCard);
			tryNum++;
			if (!success) {
				System.err.println(tryNum + ": " + punchCard.getErrorMsg());
			}
		} while (!success && tryNum < Config.MAX_PUCH_CARD_TRY_TIME);
		if (success == false) {
			System.err.println(">>>打卡失败... 尝试次数: " + tryNum);
		} else {
			System.out.println(">>>成功打卡...");
		}
		changeState(context);
		return success;
	}

	abstract boolean execPunchCard(PunchCard punchCard);

	abstract void changeState(PunchCardContext context);

	public Date getPunchCardTime() {
		// 获取一个随机的打卡时间
		Calendar punchCardStart = Calendar.getInstance();
		Map<String, String> hms = null;
		try {
			hms = DateUtil.getHMSFromFormatDate(getPunchCardStartTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		punchCardStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hms.get("h")));
		punchCardStart.set(Calendar.MINUTE, Integer.valueOf(hms.get("m")));
		punchCardStart.set(Calendar.SECOND, Integer.valueOf(hms.get("s")));

		//过了打卡时间，就把打卡任务推迟到第二天
		//TODO 转换到下一个打卡状态好一点
		if (!DateUtil.date2AfterDate1(new Date(), punchCardStart.getTime())) {
			punchCardStart.add(Calendar.DAY_OF_MONTH, 1);
		}

		Calendar punchCardEnd = Calendar.getInstance();
		punchCardEnd.setTime(punchCardStart.getTime());
		punchCardEnd.add(Calendar.MINUTE, Config.PUCH_CARD_GAP_MINUTES);

		List<Date> dates = DateUtil.getDatesBetweenTwoDate(punchCardStart.getTime(), punchCardEnd.getTime());
		Random random = new Random();
		int index = random.nextInt(dates.size());
		Date punchCardTime = dates.get(index);
		
		//周末顺延
		Calendar temp = Calendar.getInstance();
		if (DateUtil.getDayOfWeek(punchCardTime) == 6) {
			temp.setTime(punchCardTime);
			temp.add(Calendar.DAY_OF_MONTH, 2);
			return temp.getTime();
		} else if (DateUtil.getDayOfWeek(punchCardTime) == 7) {
			temp.setTime(punchCardTime);
			temp.add(Calendar.DAY_OF_MONTH, 1);
			return temp.getTime();
		}
		return punchCardTime;
	}
	
	abstract String getPunchCardStartTime();

}
