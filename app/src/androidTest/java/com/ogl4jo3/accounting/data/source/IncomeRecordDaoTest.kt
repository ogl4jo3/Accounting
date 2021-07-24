package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.utils.testAccounts
import com.ogl4jo3.accounting.utils.testDateArray
import com.ogl4jo3.accounting.utils.testIncomeCategories
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.Date


@RunWith(AndroidJUnit4::class)
class IncomeRecordDaoTest {

    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }
    private lateinit var database: AppDatabase
    private lateinit var incomeRecordDao: IncomeRecordDao

    @Before
    fun initDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        incomeRecordDao = database.incomeRecordDao()
        testAccounts.forEach { database.accountDao().insertAccount(it) }
        testIncomeCategories.forEach { database.categoryDao().insertCategory(it) }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-InsertIncomeRecord_And_GetAllRecords`() = runBlocking {
        Timber.d("Test-InsertIncomeRecord_And_GetAllRecords")
        incomeRecordDao.insertIncomeRecord(
            IncomeRecord(
                price = 50,
                accountId = testAccounts[0].id,
                categoryId = testIncomeCategories[0].id,
                description = "測試用收入",
                recordTime = Date()
            )
        )
        Assert.assertEquals(1, incomeRecordDao.getAllRecords().size)
    }

    @Test
    fun `Test-InsertIncomeRecord_And_GetIncomeRecordsByDate`() = runBlocking {
        Timber.d("Test-InsertIncomeRecord_And_GetIncomeRecordsByDate")
        incomeRecordDao.insertIncomeRecord(
            IncomeRecord(
                price = 50,
                accountId = testAccounts[0].id,
                categoryId = testIncomeCategories[0].id,
                description = "測試用收入-1",
                recordTime = testDateArray[0]
            )
        )
        incomeRecordDao.insertIncomeRecord(
            IncomeRecord(
                price = 150,
                accountId = testAccounts[0].id,
                categoryId = testIncomeCategories[0].id,
                description = "測試用收入-2",
                recordTime = testDateArray[1]
            )
        )
        incomeRecordDao.insertIncomeRecord(
            IncomeRecord(
                price = 250,
                accountId = testAccounts[0].id,
                categoryId = testIncomeCategories[0].id,
                description = "測試用收入-3",
                recordTime = testDateArray[1]
            )
        )
        incomeRecordDao.insertIncomeRecord(
            IncomeRecord(
                price = 550,
                accountId = testAccounts[0].id,
                categoryId = testIncomeCategories[0].id,
                description = "測試用收入-4",
                recordTime = testDateArray[2]
            )
        )
        Assert.assertEquals(
            2,
            incomeRecordDao.getIncomeRecordsByDate(
                testDateArray[1].beginOfDay.time, testDateArray[1].endOfDay.time
            ).size
        )
    }

    @Test
    fun `Test-InsertIncomeRecord_And_Update`() = runBlocking {
        Timber.d("Test-InsertIncomeRecord_And_Update")
        val incomeRecord = IncomeRecord(
            price = 50,
            accountId = testAccounts[0].id,
            categoryId = testIncomeCategories[0].id,
            description = "測試用收入",
            recordTime = testDateArray[0]
        )
        incomeRecordDao.insertIncomeRecord(incomeRecord)
        Assert.assertEquals(1, incomeRecordDao.getAllRecords().size)
        incomeRecord.price = 100
        incomeRecordDao.updateIncomeRecord(incomeRecord)
        Assert.assertEquals(1, incomeRecordDao.getAllRecords().size)
        Assert.assertEquals(100, incomeRecordDao.getAllRecords()[0].price)
    }

    @Test
    fun `Test-InsertIncomeRecord_And_Delete`() = runBlocking {
        Timber.d("Test-InsertIncomeRecord_And_Delete")
        val incomeRecord = IncomeRecord(
            price = 50,
            accountId = testAccounts[0].id,
            categoryId = testIncomeCategories[0].id,
            description = "測試用收入",
            recordTime = testDateArray[0]
        )
        incomeRecordDao.insertIncomeRecord(incomeRecord)
        Assert.assertEquals(1, incomeRecordDao.getAllRecords().size)
        incomeRecordDao.deleteIncomeRecord(incomeRecord)
        Assert.assertEquals(0, incomeRecordDao.getAllRecords().size)
    }

}