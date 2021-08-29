package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification

class DefaultAccountingNotificationDataSource(
    val notificationDao: AccountingNotificationDao
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long {
        return notificationDao.insertNotification(notification)
    }

    override suspend fun updateNotification(notification: AccountingNotification) {
        notificationDao.updateNotification(notification)
    }

    override suspend fun deleteNotification(notification: AccountingNotification) {
        notificationDao.deleteNotification(notification)
    }

    override suspend fun getNotificationById(notificationId: String): AccountingNotification? {
        return notificationDao.getNotificationById(notificationId)
    }

    override suspend fun getAllNotifications(): List<AccountingNotification> {
        return notificationDao.getAllNotifications()
    }

    override suspend fun getNumberOfNotifications(): Int {
        return notificationDao.getNumberOfNotifications()
    }
}