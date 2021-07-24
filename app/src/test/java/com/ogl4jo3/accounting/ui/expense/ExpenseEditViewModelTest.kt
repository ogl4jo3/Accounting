package com.ogl4jo3.accounting.ui.expense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeExpenseRecordDataSource
import com.ogl4jo3.accounting.testAccounts
import com.ogl4jo3.accounting.testDateArray
import com.ogl4jo3.accounting.testExpenseCategories
import com.ogl4jo3.accounting.testExpenseRecords
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExpenseEditViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExpenseEditViewModel
    private lateinit var fakeExpenseRecordDataSource: FakeExpenseRecordDataSource

    @Before
    fun setup() {
        fakeExpenseRecordDataSource =
            FakeExpenseRecordDataSource(testExpenseRecords.toMutableList())
        viewModel = ExpenseEditViewModel(
            FakeAccountDataSource(testAccounts.toMutableList()),
            FakeCategoryDataSource(testExpenseCategories.toMutableList()),
            fakeExpenseRecordDataSource,
            testExpenseRecords[0]
        )
    }

    @Test
    fun `test save expenseRecord`() = runBlocking {
        val expenseRecord = fakeExpenseRecordDataSource.getRecord(0)
        expenseRecord.price = 999
        expenseRecord.accountId = testAccounts[1].id
        expenseRecord.categoryId = testExpenseCategories[2].id
        expenseRecord.recordTime = testDateArray[1]
        expenseRecord.description = "test1234"
        viewModel.saveExpenseRecord(expenseRecord)
        Assert.assertEquals(999, fakeExpenseRecordDataSource.expenseRecords[0].price)
        Assert.assertEquals(
            testAccounts[1].id, fakeExpenseRecordDataSource.expenseRecords[0].accountId
        )
        Assert.assertEquals(
            testExpenseCategories[2].id, fakeExpenseRecordDataSource.expenseRecords[0].categoryId
        )
        Assert.assertEquals(
            testDateArray[1], fakeExpenseRecordDataSource.expenseRecords[0].recordTime
        )
        Assert.assertEquals("test1234", fakeExpenseRecordDataSource.expenseRecords[0].description)
    }

    @Test
    fun `test save expense record failed because price==0`() = runBlocking {
        val expenseRecord = fakeExpenseRecordDataSource.getRecord(0)
        expenseRecord.price = 0
        expenseRecord.description = "test1234"
        viewModel.saveExpenseRecord(expenseRecord)
        Assert.assertEquals(
            testExpenseRecords[0].price,
            fakeExpenseRecordDataSource.expenseRecords[0].price
        )
        Assert.assertEquals(
            testExpenseRecords[0].description,
            fakeExpenseRecordDataSource.expenseRecords[0].description
        )
    }

    @Test
    fun `test delete expenseRecord`() = runBlocking {
        val size = fakeExpenseRecordDataSource.expenseRecords.size
        viewModel.deleteExpenseRecord()
        Assert.assertEquals(size - 1, fakeExpenseRecordDataSource.expenseRecords.size)
    }

}