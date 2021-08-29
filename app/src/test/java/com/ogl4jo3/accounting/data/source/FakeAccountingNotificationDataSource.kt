package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification

class FakeAccountingNotificationDataSource(
    var notifications: MutableList<AccountingNotification> = mutableListOf()
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long {
        return if (notifications.find { it.time == notification.time } != null) {
            -1
        } else {
            notifications.add(notification)
            0
        }
    }

    override suspend fun updateNotification(notification: AccountingNotification) {
        notifications.replaceAll { if (it.id == notification.id) notification else it }
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
}