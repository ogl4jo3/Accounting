package com.example.ogl4jo3.utility.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
