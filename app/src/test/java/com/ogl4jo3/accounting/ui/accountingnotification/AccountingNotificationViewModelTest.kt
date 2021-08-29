package com.ogl4jo3.accounting.ui.accountingnotification

import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.FakeAccountingNotificationDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccountingNotificationViewModelTest {

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
            AccountingNotification(
                time = "05:30",
                isOn = true,
            )
        )
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
    }

    @Test
    fun `test add duplicate time notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        accountingNotificationViewModel.addNotification(
            AccountingNotification(
                time = "05:30",
                isOn = true,
            )
        )
        accountingNotificationViewModel.addNotification(
            AccountingNotification(
                time = "05:30",
                isOn = false,
            )
        )
        Assert.assertEquals(size + 1, fakeAccountingNotificationDataSource.notifications.size)
    }

    @Test
    fun `test get all notification`() = runBlocking {
        val size = fakeAccountingNotificationDataSource.notifications.size
        accountingNotificationViewModel.addNotification(
            AccountingNotification(
                time = "05:30",
                isOn = true,
            )
        )
        accountingNotificationViewModel.addNotification(
            AccountingNotification(
                time = "15:30",
                isOn = false,
            )
        )
        Assert.assertEquals(size + 2, fakeAccountingNotificationDataSource.notifications.size)
        val notifications = fakeAccountingNotificationDataSource.getAllNotifications()
        Assert.assertEquals(2, notifications.size)
    }

}