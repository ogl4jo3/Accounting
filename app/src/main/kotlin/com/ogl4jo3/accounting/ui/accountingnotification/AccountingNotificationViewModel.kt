package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.common.launchInWithDefaultErrorHandler
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar

class AccountingNotificationViewModel(
    private val notificationDataSource: AccountingNotificationDataSource,
    private val alarmSetter: AlarmSetter,
) : ViewModel() {

    var updateFailed: () -> Unit = {}
    var showTimePickerDialog: (
        notification: AccountingNotification,
        notifyItemChanged: (notification: AccountingNotification) -> Unit
    ) -> Unit = { _, _ -> }

    private val _allNotifications: MutableLiveData<List<AccountingNotification>> =
        MutableLiveData(emptyList())
    val allNotifications: LiveData<List<AccountingNotification>> = _allNotifications

    fun updateNotificationList() {
        notificationDataSource.getAllNotifications()
            .onEach { _allNotifications.value = it }
            .launchInWithDefaultErrorHandler(viewModelScope)
    }

    fun updateNotification(
        id: String, hour: Int, minute: Int, isOn: Boolean,
        notifyItemChanged: (notification: AccountingNotification) -> Unit = {}
    ) {
        val notification = AccountingNotification(id, hour, minute, isOn)
        notificationDataSource.updateNotification(notification)
            .onEach { isSuccessful ->
                if (isSuccessful) {
                    notifyItemChanged(notification)
                    updateAlarm(notification)
                } else {
                    Timber.e("updateNotification failed")
                    updateFailed()
                }
            }
            .launchInWithDefaultErrorHandler(viewModelScope) {
                updateFailed()
            }
    }

    fun switchNotification(notification: AccountingNotification) {
        notificationDataSource.updateNotification(notification)
            .onEach { isSuccessful ->
                if (isSuccessful) {
                    updateAlarm(notification)
                } else {
                    Timber.e("updateNotification failed")
                    updateFailed()
                }
            }
            .launchInWithDefaultErrorHandler(viewModelScope) {
                updateFailed()
            }
    }

    private fun updateAlarm(notification: AccountingNotification) {
        when (notification.isOn) {
            true -> {
                val calendar: Calendar = Calendar.getInstance().apply {
                    val now = System.currentTimeMillis()
                    timeInMillis = now
                    set(Calendar.HOUR_OF_DAY, notification.hour)
                    set(Calendar.MINUTE, notification.minute)
                    if (timeInMillis < now) { // Avoid this situation, "If the trigger time you specify is in the past, the alarm triggers immediately."
                        add(Calendar.DATE, 1)
                    }
                }
                alarmSetter.setInexactRepeating(calendar)
            }
            false -> {
                alarmSetter.cancel()
            }
        }
    }

}