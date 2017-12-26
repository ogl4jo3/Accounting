package ogl4jo3.shaowei.ogl4jo3.accounting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountingnotification.AccountingDailyAlarmReceiver;
import ogl4jo3.shaowei.ogl4jo3.accounting.useteaching.UseTeachingActivity;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;

public class InitActivity extends AppCompatActivity {

	private static final int WAIT_SECOND = 1000;// 等候秒數

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		//取得是否第一次使用
		final boolean isFirstUse = new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
				.getBoolean(SharedPreferencesTag.prefsFirstUse, true);

		setDailyAlarm(isFirstUse);//設定每日記帳提醒

		//切頁，需要與UI Thread分開
		new Thread(new Runnable() {

			@Override
			public void run() {
				Intent mainIntent = new Intent(InitActivity.this,
						isFirstUse ? UseTeachingActivity.class : MainActivity.class);

				//等候N秒鐘
				try {
					Thread.sleep(WAIT_SECOND);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				startActivity(mainIntent);
				finish();
			}
		}).start();
	}

	/**
	 * 設置每日記帳提醒
	 */
	private void setDailyAlarm(Boolean isFirstUse) {
		if (isFirstUse) {
			//設定每日記帳提醒，預設為22:30，ON
			new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
					.setInt(SharedPreferencesTag.prefsDailyAlarmHour,
							AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR);
			new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
					.setInt(SharedPreferencesTag.prefsDailyAlarmMinute,
							AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE);
			new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
					.setBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, true);
		}
		boolean isDailyAlarmOn = new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
				.getBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, true);

		if (isDailyAlarmOn) {

			//建立意圖
			Intent intent = new Intent();
			intent.setClass(this, AccountingDailyAlarmReceiver.class);
			//建立待處理意圖
			PendingIntent pending =
					PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//取得AlarmManager
			AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			//使用Calendar指定時間
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,
					new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
							.getInt(SharedPreferencesTag.prefsDailyAlarmHour,
									AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR));
			calendar.set(Calendar.MINUTE,
					new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
							.getInt(SharedPreferencesTag.prefsDailyAlarmMinute,
									AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE));
			calendar.set(Calendar.SECOND, 0);

			alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					AlarmManager.INTERVAL_DAY, pending);
		}

	}
}
