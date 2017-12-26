package ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences;

/**
 * SharedPreferencesTag TAG
 * Created by ogl4jo3 on 2017/8/1.
 */

public interface SharedPreferencesTag {

	String prefsData = "DATA";//SharedPreferences名稱

	String prefsFirstUse = "FIRST_USE";//第一次使用
	String prefsBudgetStartDay = "BUDGET_START_DAY";//預算起始日

	String prefsDailyAlarmHour = "DAILY_ALARM_HOUR";//每日記帳提醒，時
	String prefsDailyAlarmMinute = "DAILY_ALARM_MINUTE";//每日記帳提醒，分
	String prefsDailyAlarmOnOff = "DAILY_ALARM_ON_OFF";//每日記帳提醒，On/Off
}