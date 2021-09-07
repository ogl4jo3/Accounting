package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.source.FakeAccountingNotificationDataSource
import com.ogl4jo3.accounting.testNotifications
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class AccountingNotificationViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AccountingNotificationViewModel
    private lateinit var fakeAccountingNotificationDataSource: FakeAccountingNotificationDataSource

    @Before
    fun setupViewModel() {
        fakeAccountingNotificationDataSource =
            FakeAccountingNotificationDataSource(mutableListOf(testNotifications[0]))
        viewModel =
            AccountingNotificationViewModel(fakeAccountingNotificationDataSource,
                object : AlarmSetter {
                    override fun setInexactRepeating(calendar: Calendar) {
                        println("AlarmSetter setInexactRepeating, Not yet implemented")
                    }

                    override fun cancel() {
                        println("AlarmSetter cancel, Not yet implemented")
                    }
                })
    }

    @Test
    fun `test get notification`() {
        val notification = viewModel.defaultNotification.value!!
        Assert.assertEquals(21, notification.hour)
        Assert.assertEquals(30, notification.minute)
        Assert.assertEquals(false, notification.isOn)
    }

    @Test
    fun `test update notification`() = runBlocking {
        val notification = viewModel.defaultNotification.value!!
        notification.hour = 3
        notification.minute = 34
        notification.isOn = true
        viewModel.updateNotification(notification)
        Assert.assertEquals(3, viewModel.defaultNotification.value?.hour)
        Assert.assertEquals(34, viewModel.defaultNotification.value?.minute)
        Assert.assertEquals(true, viewModel.defaultNotification.value?.isOn)
    }

    @Test
    fun `test update notification time`() = runBlocking {
        val notification = viewModel.defaultNotification.value!!
        viewModel.updateNotificationTime(3, 34, notification)
        Assert.assertEquals(3, viewModel.defaultNotification.value?.hour)
        Assert.assertEquals(34, viewModel.defaultNotification.value?.minute)
        Assert.assertEquals(false, viewModel.defaultNotification.value?.isOn)
    }

    @Test
    fun `test switch notification`() = runBlocking {
        val notification = viewModel.defaultNotification.value!!
        viewModel.switchNotification(true, notification)
        Assert.assertEquals(21, viewModel.defaultNotification.value?.hour)
        Assert.assertEquals(30, viewModel.defaultNotification.value?.minute)
        Assert.assertEquals(true, viewModel.defaultNotification.value?.isOn)
    }
}