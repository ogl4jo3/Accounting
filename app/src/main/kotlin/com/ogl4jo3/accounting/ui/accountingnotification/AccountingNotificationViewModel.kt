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
    var showTimePickerDialog: (notification: AccountingNotification) -> Unit = { }

    private val _defaultNotification = MutableLiveData<AccountingNotification>()
    val defaultNotification: LiveData<AccountingNotification> = _defaultNotification

    init {
        notificationDataSource.getDefaultNotification()
            .onEach {
                if (it == null) {
                    //TODO: return home page
                } else {
                    _defaultNotification.value = it
                }
            }.launchInWithDefaultErrorHandler(viewModelScope) {
                //TODO: return home page
            }
    }

    fun updateNotificationTime(hour: Int, minute: Int, notification: AccountingNotification) {
        notification.hour = hour
        notification.minute = minute
        updateNotification(notification)
    }

    fun switchNotification(isChecked: Boolean, notification: AccountingNotification) {
        notification.isOn = isChecked
        updateNotification(notification)
    }

    fun updateNotification(notification: AccountingNotification) {
        notificationDataSource.updateNotification(notification)
            .onEach { isSuccessful ->
                if (isSuccessful) {
                    updateAlarm(notification)
                    _defaultNotification.value = notification
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