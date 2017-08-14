package ogl4jo3.shaowei.ogl4jo3.utility.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式相關轉換
 * 格式為年月日"yyyy-MM-dd"
 * ex:2017-07-30
 * Created by ogl4jo3 on 2017/7/30.
 */

public class DateUtil {

	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 取得現在時間
	 * "yyyy-MM-dd"
	 *
	 * @return Str String
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
	}

	/**
	 * Date格式轉String
	 *
	 * @param date Date
	 * @return Str String
	 */
	public static String dateToStr(Date date) {
		return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date);
	}

	/**
	 * String格式轉Date
	 *
	 * @param str String
	 * @return date Date
	 */
	public static Date strToDate(String str) throws ParseException {
		return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).parse(str);
	}

	/**
	 * 支出、收入統計用
	 * 計算開始日期
	 * return fromDate "yyyy-MM-dd"
	 */
	public static String fromDateStrByMonth(Date date, String budgetStartDay) {
		String yearMonthStr = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(date);
		String fromDateStr;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//若設定的預算起始日超過這個月份最大天數時，改為這個月份最大日期
		if (Integer.valueOf(budgetStartDay) > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			fromDateStr = yearMonthStr + "-" +
					String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			fromDateStr = yearMonthStr + "-" + budgetStartDay;
		}
		return fromDateStr;
	}

	/**
	 * 支出、收入統計用
	 * 計算結束日期
	 * return toDate"yyyy-MM-dd"
	 */
	public static String toDateStrByMonth(Date date, String budgetStartDay) {
		String toDateStr;
		//若設定的預算起始日為01取得本月最後一天的日期，否則設定為預算起始日-1
		if (budgetStartDay.equals("01")) {
			toDateStr = DateUtil.getLastDayOfTheMonth(date);
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(budgetStartDay) - 1);
			toDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
					.format(calendar.getTime());
		}
		return toDateStr;
	}

	/**
	 * 取得此月份最一天的日期
	 * ex:2017-7月 return 2017-07-31，2017-6月 return 2017-06-30
	 *
	 * @return Str String
	 */
	public static String getLastDayOfTheMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(c.getTime());
	}
}
