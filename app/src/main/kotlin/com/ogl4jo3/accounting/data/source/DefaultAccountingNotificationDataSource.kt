package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import timber.log.Timber

class DefaultAccountingNotificationDataSource(
    private val notificationDao: AccountingNotificationDao
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long {
        return if (notification.is24HFormat()) {
            notificationDao.insertNotification(notification)
        } else {
            Timber.e("insertNotification failed, notification: $notification")
            -1
        }
    }

    //TODO: maybe return successful or throw exception
    override suspend fun updateNotification(notification: AccountingNotification) {
        if (notification.is24HFormat()) {
            notificationDao.updateNotification(notification)
        } else {
            Timber.e("updateNotification failed, notification: $notification")
        }
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