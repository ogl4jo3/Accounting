package com.ogl4jo3.accounting.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import com.ogl4jo3.accounting.ui.accountingnotification.AlarmSetter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class AccountingBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val notificationDataSource: AccountingNotificationDataSource = GlobalContext.get().get()
            val alarmSetter: AlarmSetter = GlobalContext.get().get()
            GlobalScope.launch {
                notificationDataSource.getDefaultNotification()?.let { notification ->
                    if (notification.isOn) {
                        alarmSetter.setInexactRepeating(notification.hour, notification.minute)
                    }
                }
            }
        }
    }

}