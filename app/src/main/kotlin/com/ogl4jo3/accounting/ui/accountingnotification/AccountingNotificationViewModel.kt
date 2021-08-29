package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AccountingNotificationViewModel(
    private val notificationDataSource: AccountingNotificationDataSource
) : ViewModel() {

    private val _allNotifications: MutableLiveData<List<AccountingNotification>> =
        MutableLiveData(emptyList())
    val allNotifications: LiveData<List<AccountingNotification>> = _allNotifications

    fun updateAllNotifications() {
        _allNotifications.value = runBlocking { notificationDataSource.getAllNotifications() }
    }

    //TODO: deleteNotification, size>=1

    fun addNotification(notification: AccountingNotification) {
        //TODO: size <= 3
        //TODO: check time format 00:00 ~ 23:59
        runBlocking {
            val id = notificationDataSource.insertNotification(notification)
            if (id < 0) {
                Timber.e("insertNotification failed, notification: $notification")
            } else {
                //TODO: success
            }
        }
    }

}