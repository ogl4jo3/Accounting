package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAccountingNotificationDataSource(
    private var notifications: MutableList<AccountingNotification> = mutableListOf()
) : AccountingNotificationDataSource {

    override fun insertNotification(notification: AccountingNotification): Flow<Long> {
        return flow {
            if (notifications.find { it.hour == notification.hour && it.minute == notification.minute } != null
                || !notification.is24HFormat()
                || notifications.size >= AccountingNotificationDataSource.MAX_SIZE) {
                emit(-1)
            } else {
                notifications.add(notification)
                emit(0)
            }
        }
    }

    override fun updateNotification(notification: AccountingNotification): Flow<Boolean> {
        return flow {
            if (notification.is24HFormat()) {
                notifications.replaceAll { if (it.id == notification.id) notification else it }
                emit(true)
            } else {
                emit(false)
            }
        }
    }

    override fun getDefaultNotification(): Flow<AccountingNotification?> {
        return flow {
            if (notifications.size != 1) {
                emit(null)
            } else {
                emit(notifications[0].copy())
            }
        }
    }

    override fun getAllNotifications(): Flow<List<AccountingNotification>> {
        return flow {
            emit(notifications)
        }
    }

    override fun getNumberOfNotifications(): Flow<Int> {
        return flow {
            emit(notifications.size)
        }
    }

}