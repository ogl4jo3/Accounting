package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.utils.testAccounts
import com.ogl4jo3.accounting.utils.testDateArray
import com.ogl4jo3.accounting.utils.testExpenseCategories
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.Date


@RunWith(AndroidJUnit4::class)
class ExpenseRecordDaoTest {

    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }
    private lateinit var database: AppDatabase
    private lateinit var expenseRecordDao: ExpenseRecordDao

    @Before
    fun initDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        expenseRecordDao = database.expenseRecordDao()
        testAccounts.forEach { database.accountDao().insertAccount(it) }
        testExpenseCategories.forEach { database.categoryDao().insertCategory(it) }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-InsertExpenseRecord_And_GetAllRecords`() = runBlocking {
        Timber.d("Test-InsertExpenseRecord_And_GetAllRecords")
        expenseRecordDao.insertExpenseRecord(
            ExpenseRecord(
                price = 50,
                accountId = testAccounts[0].id,
                categoryId = testExpenseCategories[0].id,
                description = "測試用支出",
                recordTime = Date()
            )
        )
        Assert.assertEquals(1, expenseRecordDao.getAllRecords().size)
    }

    @Test
    fun `Test-InsertExpenseRecord_And_GetExpenseRecordsByDate`() = runBlocking {
        Timber.d("Test-InsertExpenseRecord_And_GetAllRecords")
        expenseRecordDao.insertExpenseRecord(
            ExpenseRecord(
                price = 50,
                accountId = testAccounts[0].id,
                categoryId = testExpenseCategories[0].id,
                description = "測試用支出-1",
                recordTime = testDateArray[0]
            )
        )
        expenseRecordDao.insertExpenseRecord(
            ExpenseRecord(
                price = 150,
                accountId = testAccounts[0].id,
                categoryId = testExpenseCategories[0].id,
                description = "測試用支出-2",
                recordTime = testDateArray[1]
            )
        )
        expenseRecordDao.insertExpenseRecord(
            ExpenseRecord(
                price = 250,
                accountId = testAccounts[0].id,
                categoryId = testExpenseCategories[0].id,
                description = "測試用支出-3",
                recordTime = testDateArray[1]
            )
        )
        expenseRecordDao.insertExpenseRecord(
            ExpenseRecord(
                price = 550,
                accountId = testAccounts[0].id,
                categoryId = testExpenseCategories[0].id,
                description = "測試用支出-4",
                recordTime = testDateArray[2]
            )
        )
        Assert.assertEquals(
            2,
            expenseRecordDao.getExpenseRecordsByDate(
                testDateArray[1].beginOfDay.time, testDateArray[1].endOfDay.time
            ).size
        )
    }
}