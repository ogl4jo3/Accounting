package com.ogl4jo3.accounting.ui.expense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeExpenseRecordDataSource
import com.ogl4jo3.accounting.testAccounts
import com.ogl4jo3.accounting.testExpenseCategories
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

class ExpenseAddViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExpenseAddViewModel
    private lateinit var fakeExpenseRecordDataSource: FakeExpenseRecordDataSource

    @Before
    fun setup() {
        fakeExpenseRecordDataSource = FakeExpenseRecordDataSource()
        viewModel = ExpenseAddViewModel(
            FakeAccountDataSource(testAccounts.toMutableList()),
            FakeCategoryDataSource(testExpenseCategories.toMutableList()),
            fakeExpenseRecordDataSource,
            Date()
        )
    }

    @Test
    fun `test add expense record`() = runBlocking {
        val expenseRecordSize = fakeExpenseRecordDataSource.expenseRecords.size
        viewModel.addExpenseRecord(
            ExpenseRecord(
                price = 50,
                accountId = "0",
                categoryId = "0",
                description = "測試用支出",
                recordTime = viewModel.date
            )
        )
        Assert.assertEquals(expenseRecordSize + 1, fakeExpenseRecordDataSource.expenseRecords.size)
    }

    @Test
    fun `test add expense record failed because price==0`() = runBlocking {
        val expenseRecordSize = fakeExpenseRecordDataSource.expenseRecords.size
        viewModel.addExpenseRecord(
            ExpenseRecord(
                price = 0,
                accountId = "0",
                categoryId = "0",
                description = "測試用支出",
                recordTime = viewModel.date
            )
        )
        Assert.assertEquals(expenseRecordSize, fakeExpenseRecordDataSource.expenseRecords.size)
    }
}