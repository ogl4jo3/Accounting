package com.ogl4jo3.accounting.setting.accountingnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ogl4jo3.accounting.MainActivity;
import com.ogl4jo3.accounting.R;

/**
 * 每日記帳提醒
 * 繼承自BroadcastReceiver的廣播接收元件
 * Created by ogl4jo3 on 2017/12/26.
 */

public class AccountingDailyAlarmReceiver extends BroadcastReceiver {

	public static final int DEFAULT_DAILY_ALARM_HOUR = 22;
	public static final int DEFAULT_DAILY_ALARM_MINUTE = 30;

	public AccountingDailyAlarmReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 執行廣播元件的工作
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_accounting)
						.setContentTitle(context.getString(R.string.notification_daily_alarm))
						.setContentText(
								context.getString(R.string.notification_daily_alarm_content))
						.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
		//點按通知直接返回至新增頁，並將類別輸入完成
		Intent resultIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());

	}
}
