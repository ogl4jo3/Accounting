package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.FakeAccountingNotificationDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountingNotificationViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var accountingNotificationViewModel: AccountingNotificationViewModel
    private lateinit var fakeAccountingNotificationDataSource: FakeAccountingNotificationDataSource

    @Before
    fun setupViewModel() {
        fakeAccountingNotificationDataSource = FakeAccountingNotificationDataSource()
        accountingNotificationViewModel =
            AccountingNotificationViewModel(fakeAccountingNotificationDataSource)
    }

    @Test
    fun `test add notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        accountingNotificationViewModel.addNotification(
            AccountingNotification(hour = 5, minute = 30, isOn = true)
        )
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
    }

    @Test
    fun `test add duplicate time notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        accountingNotificationViewModel.addNotification(
            AccountingNotification(hour = 5, minute = 30, isOn = true)
        )
        accountingNotificationViewModel.addNotification(
            AccountingNotification(hour = 5, minute = 30, isOn = false)
        )
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
    }

    @Test
    fun `test get all notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        accountingNotificationViewModel.addNotification(
            AccountingNotification(hour = 5, minute = 30, isOn = true)
        )
        accountingNotificationViewModel.addNotification(
            AccountingNotification(hour = 15, minute = 30, isOn = false)
        )
        Assert.assertEquals(size + 2, fakeAccountingNotificationDataSource.notifications.size)
        val notifications = fakeAccountingNotificationDataSource.getAllNotifications()
        Assert.assertEquals(2, notifications.size)
    }

    @Test
    fun `test update notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        val notification = AccountingNotification(hour = 5, minute = 30, isOn = true)
        accountingNotificationViewModel.addNotification(notification)
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
        notification.hour = 16
        notification.minute = 25
        accountingNotificationViewModel.updateNotification(notification)
        Assert.assertEquals(16, fakeAccountingNotificationDataSource.getNotification(0).hour)
        Assert.assertEquals(25, fakeAccountingNotificationDataSource.getNotification(0).minute)
    }

    @Test
    fun `test update duplicate time notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        val notification = AccountingNotification(hour = 5, minute = 30, isOn = true)
        val notification2 = AccountingNotification(hour = 16, minute = 30, isOn = true)
        accountingNotificationViewModel.addNotification(notification)
        accountingNotificationViewModel.addNotification(notification2)
        Assert.assertEquals(size + 2, fakeAccountingNotificationDataSource.notifications.size)
        val updatedNotification =
            AccountingNotification(id = notification2.id, hour = 16, minute = 30, isOn = true)
        accountingNotificationViewModel.updateNotification(updatedNotification)
        Assert.assertEquals(5, fakeAccountingNotificationDataSource.getNotification(0).hour)
        Assert.assertEquals(30, fakeAccountingNotificationDataSource.getNotification(0).minute)
    }

    @Test
    fun `test delete notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        val notification = AccountingNotification(hour = 5, minute = 30, isOn = true)
        val notification2 = AccountingNotification(hour = 15, minute = 30, isOn = true)
        accountingNotificationViewModel.addNotification(notification)
        accountingNotificationViewModel.addNotification(notification2)
        Assert.assertEquals(size + 2, fakeAccountingNotificationDataSource.notifications.size)
        accountingNotificationViewModel.deleteNotification(notification)
        Assert.assertEquals(1, fakeAccountingNotificationDataSource.notifications.size)
    }

    @Test
    fun `test delete notification - fail , at least one notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        val notification = AccountingNotification(hour = 5, minute = 30, isOn = true)
        accountingNotificationViewModel.addNotification(notification)
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
        accountingNotificationViewModel.deleteNotification(notification)
        Assert.assertEquals(1, fakeAccountingNotificationDataSource.notifications.size)
    }

}