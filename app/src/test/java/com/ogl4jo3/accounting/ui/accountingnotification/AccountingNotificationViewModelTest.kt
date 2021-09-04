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
    fun setupViewModel() {
        fakeAccountingNotificationDataSource =
            FakeAccountingNotificationDataSource(testNotifications.toMutableList())
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
        accountingNotificationViewModel.updateNotificationList()
    }

    @Test
    fun `test get all notification`() {
        val notifications = accountingNotificationViewModel.allNotifications.value
        Assert.assertEquals(1, notifications?.size)
    }

    @Test
    fun `test update notification`() = runBlocking {
        val notification = accountingNotificationViewModel.allNotifications.value?.get(0)!!
        accountingNotificationViewModel.updateNotification(notification.id, 16, 25, true)
        Assert.assertEquals(16, fakeAccountingNotificationDataSource.getNotification(0).hour)
        Assert.assertEquals(25, fakeAccountingNotificationDataSource.getNotification(0).minute)
    }

}