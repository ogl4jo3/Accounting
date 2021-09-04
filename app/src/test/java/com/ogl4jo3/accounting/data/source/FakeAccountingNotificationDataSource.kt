package com.ogl4jo3.accounting.data.source

import androidx.annotation.VisibleForTesting
import com.ogl4jo3.accounting.data.AccountingNotification

class FakeAccountingNotificationDataSource(
    private var notifications: MutableList<AccountingNotification> = mutableListOf()
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long {
        return if (notifications.find { it.hour == notification.hour && it.minute == notification.minute } != null
            || !notification.is24HFormat()) {
            -1
        } else {
            notifications.add(notification)
            0
        }
    }

    override suspend fun updateNotification(notification: AccountingNotification) {
        if (notification.is24HFormat()) {
            notifications.replaceAll { if (it.id == notification.id) notification else it }
        }
    }

    override suspend fun deleteNotification(notification: AccountingNotification) {
        notifications.remove(notification)
    }

    override suspend fun getNotificationById(notificationId: String): AccountingNotification? {
        return notifications.find { it.id == notificationId }
    }

    override suspend fun getAllNotifications(): List<AccountingNotification> {
        return notifications
    }

    override suspend fun getNumberOfNotifications(): Int {
        return notifications.size
    }

    @VisibleForTesting
    fun getNotification(index: Int): AccountingNotification {
        return notifications[index].copy()
    }
}