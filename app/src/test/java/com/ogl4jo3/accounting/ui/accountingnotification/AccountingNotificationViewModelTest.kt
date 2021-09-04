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

    private lateinit var accountingNotificationViewModel: AccountingNotificationViewModel
    private lateinit var fakeAccountingNotificationDataSource: FakeAccountingNotificationDataSource

    @Before
    fun setupViewModel() = runBlocking {
        fakeAccountingNotificationDataSource = FakeAccountingNotificationDataSource()
        fakeAccountingNotificationDataSource.insertNotification(testNotifications[0])
        accountingNotificationViewModel =
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
    fun `test get all notification`() = runBlocking {
        val notifications = fakeAccountingNotificationDataSource.getAllNotifications()
        Assert.assertEquals(1, notifications.size)
    }

    @Test
    fun `test update notification`() = runBlocking {
        val notification = fakeAccountingNotificationDataSource.getAllNotifications()[0]
        notification.hour = 16
        notification.minute = 25
        accountingNotificationViewModel.updateNotification(notification)
        Assert.assertEquals(16, fakeAccountingNotificationDataSource.getNotification(0).hour)
        Assert.assertEquals(25, fakeAccountingNotificationDataSource.getNotification(0).minute)
    }

}