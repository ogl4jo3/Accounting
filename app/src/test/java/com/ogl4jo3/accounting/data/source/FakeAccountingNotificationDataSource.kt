package com.ogl4jo3.accounting.data.source

import androidx.annotation.VisibleForTesting
import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAccountingNotificationDataSource(
    private var notifications: MutableList<AccountingNotification> = mutableListOf()
) : AccountingNotificationDataSource {

    override fun insertNotification(notification: AccountingNotification): Flow<Long> {
        return flow {
            if (notifications.find { it.hour == notification.hour && it.minute == notification.minute } != null
                || !notification.is24HFormat()) {
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

    @VisibleForTesting
    fun getNotification(index: Int): AccountingNotification {
        return notifications[index].copy()
    }
}