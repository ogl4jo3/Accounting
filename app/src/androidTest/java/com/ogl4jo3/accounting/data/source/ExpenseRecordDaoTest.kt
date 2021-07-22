package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.utils.testAccounts
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

}