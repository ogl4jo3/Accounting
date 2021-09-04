package com.ogl4jo3.accounting.ui.accountingnotification

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.ogl4jo3.accounting.receiver.AlarmReceiver
import java.util.Calendar

class AccountingAlarmSetter(
    app: Application,
    private val alarmManager: AlarmManager
) : AlarmSetter {

    private val notifyIntent = Intent(app, AlarmReceiver::class.java)
    private val notifyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        app,
        REQUEST_CODE,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    override fun setInexactRepeating(calendar: Calendar) {
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            notifyPendingIntent
        )
    }

    override fun cancel() {
        alarmManager.cancel(notifyPendingIntent)
    }

    companion object {
        private const val REQUEST_CODE = 0
    }
}