package com.ogl4jo3.accounting.setting.accountingnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ogl4jo3.accounting.MainActivity;
import com.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;

/**
 * 記帳提醒
 * 繼承自BroadcastReceiver的廣播接收元件
 * Created by ogl4jo3 on 2017/12/25.
 */

public class AccountingAlarmReceiver extends BroadcastReceiver {

	public static final String ACCOUNTING_ALARM = "accountingAlarm";

	public AccountingAlarmReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 執行廣播元件的工作
		Bundle bundle = intent.getExtras();
		int accountingNotificationId = bundle.getInt(AccountingNotificationDAO.KEY_ID);
		SQLiteDatabase db = MyDBHelper.getDatabase(context);
		AccountingNotification accountingNotification =
				new AccountingNotificationDAO(db).getById(accountingNotificationId);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(accountingNotification.getCategoryIcon())
				.setContentTitle(accountingNotification.getNotificationName())
				.setContentText(accountingNotification.getCategoryName())
				.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
		//點按通知直接返回至新增頁，並將類別輸入完成
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra(ACCOUNTING_ALARM, ACCOUNTING_ALARM);
		resultIntent.putExtra(ExpensesDAO.CATEGORY_COLUMN, accountingNotification.getCategoryId());
		resultIntent.putExtra(ExpensesDAO.DESCRIPTION_COLUMN,
				accountingNotification.getNotificationName());
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder
				.getPendingIntent(accountingNotificationId, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(accountingNotificationId, mBuilder.build());

	}
}
