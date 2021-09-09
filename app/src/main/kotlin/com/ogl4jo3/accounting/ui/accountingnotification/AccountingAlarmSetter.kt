package com.ogl4jo3.accounting.ui.accountingnotification

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import com.ogl4jo3.accounting.receiver.AccountingBootReceiver
import com.ogl4jo3.accounting.receiver.AlarmReceiver
import java.util.Calendar

class AccountingAlarmSetter(
    private val app: Application,
    private val alarmManager: AlarmManager
) : AlarmSetter {

    private val notifyIntent = Intent(app, AlarmReceiver::class.java)
    private val notifyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        app,
        REQUEST_CODE,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    override fun setInexactRepeating(hour: Int, minute: Int) {
        val calendar: Calendar = Calendar.getInstance().apply {
            val now = System.currentTimeMillis()
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            if (timeInMillis < now) { // Avoid this situation, "If the trigger time you specify is in the past, the alarm triggers immediately."
                add(Calendar.DATE, 1)
            }
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            notifyPendingIntent
        )
        app.packageManager.setComponentEnabledSetting(
            ComponentName(app, AccountingBootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun cancel() {
        alarmManager.cancel(notifyPendingIntent)
        app.packageManager.setComponentEnabledSetting(
            ComponentName(app, AccountingBootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    companion object {
        private const val REQUEST_CODE = 0
    }
}