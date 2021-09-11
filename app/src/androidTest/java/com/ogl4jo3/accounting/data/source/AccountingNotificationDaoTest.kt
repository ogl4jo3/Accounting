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
    private val notificationA = AccountingNotification(hour = 5, minute = 30, isOn = true)

    @Before
    fun initDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        accountNotificationDao = database.accountingNotificationDao()
        val res = accountNotificationDao.insertNotification(notificationA)
    }


    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-GetAllAccount`() = runBlocking {
        Timber.d("Test-GetAllAccount")
        val notifications = accountNotificationDao.getAllNotifications()
        Assert.assertEquals(1, notifications.size)
    }

    @Test
    fun `Test-GetById`() = runBlocking {
        Timber.d("Test-GetById")
        val notification = accountNotificationDao.getNotificationById(notificationA.id)
        Timber.d("notificationA: $notificationA")
        Timber.d("notification: $notification")
        Assert.assertEquals(notificationA.hour, (notification?.hour))
        Assert.assertEquals(notificationA.minute, (notification?.minute))
        Assert.assertEquals(notificationA.isOn, (notification?.isOn))
    }

    @Test
    fun `Test-InsertDuplicateTime`() = runBlocking {
        Timber.d("Test-Insert_And_GetById")
        val notification = AccountingNotification(hour = 15, minute = 30, isOn = true)
        val notification2 = AccountingNotification(hour = 15, minute = 30, isOn = false)
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
        val notification =
            AccountingNotification(id = notificationA.id, hour = 12, minute = 34, isOn = true)

        accountNotificationDao.updateNotification(notification)
        Timber.d("updated notification: $notification")

        val updatedNotification =
            accountNotificationDao.getNotificationById(notificationA.id)
        Timber.d("updatedNotification: $updatedNotification")
        Assert.assertEquals(notification.hour, (updatedNotification?.hour))
        Assert.assertEquals(notification.minute, (updatedNotification?.minute))
        Assert.assertEquals(notification.isOn, (updatedNotification?.isOn))
    }

}