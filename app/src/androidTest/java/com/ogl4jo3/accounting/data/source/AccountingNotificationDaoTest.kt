package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
class AccountingNotificationDaoTest {

    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }
    private lateinit var database: AppDatabase
    private lateinit var accountNotificationDao: AccountingNotificationDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        accountNotificationDao = database.accountingNotificationDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-Insert_And_GetAllAccount`() = runBlocking {
        Timber.d("Test-Insert_And_GetAllAccount")
        accountNotificationDao.insertNotification(
            AccountingNotification(
                time = "05:30",
                isOn = true,
            )
        )
        accountNotificationDao.insertNotification(
            AccountingNotification(
                time = "16:30",
                isOn = false,
            )
        )
        Assert.assertEquals(2, accountNotificationDao.getAllNotifications().size)
    }

    @Test
    fun `Test-Insert_And_GetById`() = runBlocking {
        Timber.d("Test-Insert_And_GetById")
        val notification = AccountingNotification(
            time = "05:30",
            isOn = true,
        )
        accountNotificationDao.insertNotification(notification)
        Timber.d("notification: $notification")

        accountNotificationDao.getNotificationById(notification.id)?.let { notificationLoaded ->
            Timber.d("notificationLoaded: $notificationLoaded")
            Assert.assertEquals(notificationLoaded.time, (notification.time))
            Assert.assertEquals(notificationLoaded.isOn, (notification.isOn))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-InsertDuplicateTime`() = runBlocking {
        Timber.d("Test-Insert_And_GetById")
        val notification = AccountingNotification(
            time = "05:30",
            isOn = true,
        )
        val notification2 = AccountingNotification(
            time = "05:30",
            isOn = false,
        )
        val res = accountNotificationDao.insertNotification(notification)
        Timber.d("res: $res")
        Timber.d("notification: $notification")
        Assert.assertNotEquals(-1, res)

        val res2 = accountNotificationDao.insertNotification(notification2)
        Timber.d("res2: $res2")
        Timber.d("notification2: $notification2")
        Assert.assertEquals(-1, res2)
    }

    @Test
    fun `Test-GetNullNotification`() = runBlocking {
        Timber.d("Test-GetNullNotification")
        val notification = accountNotificationDao.getNotificationById("00")
        Timber.d("notification: $notification")
        Assert.assertNull(notification)
    }

    @Test
    fun `Test-Update_And_GetById`() = runBlocking {
        Timber.d("Test-Update_And_GetById")
        val notification = AccountingNotification(
            time = "05:30",
            isOn = true,
        )
        accountNotificationDao.insertNotification(notification)
        Timber.d("notification: $notification")

        notification.time = "12:34"
        notification.isOn = true
        accountNotificationDao.updateNotification(notification)
        Timber.d("updated notification: $notification")

        accountNotificationDao.getNotificationById(notification.id)?.let { notificationLoaded ->
            Timber.d("notificationLoaded: $notificationLoaded")
            Assert.assertEquals(notificationLoaded.time, (notification.time))
            Assert.assertEquals(notificationLoaded.isOn, (notification.isOn))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-DeleteNotification`() = runBlocking {
        Timber.d("Test-DeleteNotification")
        val notification = AccountingNotification(
            time = "05:30",
            isOn = true,
        )
        accountNotificationDao.insertNotification(notification)
        Assert.assertNotNull(accountNotificationDao.getNotificationById(notification.id))

        accountNotificationDao.deleteNotification(notification)
        Assert.assertNull(accountNotificationDao.getNotificationById(notification.id))
    }

}