package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification

class FakeAccountingNotificationDataSource(
    private var notifications: MutableList<AccountingNotification> = mutableListOf()
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long {
        return if (notifications.find { it.hour == notification.hour && it.minute == notification.minute } != null
            || !notification.is24HFormat()
            || notifications.size >= AccountingNotificationDataSource.MAX_SIZE) {
            -1
        } else {
            notifications.add(notification)
            0
        }
    }


    override suspend fun updateNotification(notification: AccountingNotification): Boolean {
        return if (notification.is24HFormat()) {
            notifications.replaceAll { if (it.id == notification.id) notification else it }
            true
        } else {
            false
        }
    }

    override suspend fun getDefaultNotification(): AccountingNotification? {
        return if (notifications.size != 1) {
            null
        } else {
            notifications[0].copy()
        }
    }

    override suspend fun getAllNotifications(): List<AccountingNotification> {
        return notifications
    }

    override suspend fun getNumberOfNotifications(): Int {
        return notifications.size
    }

}