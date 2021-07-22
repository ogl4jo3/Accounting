package com.ogl4jo3.accounting.ui.expense

import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.FakeExpenseRecordDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

class ExpenseAddViewModelTest {

    private lateinit var viewModel: ExpenseAddViewModel
    private lateinit var fakeExpenseRecordDataSource: FakeExpenseRecordDataSource

    @Before
    fun setup() {
        fakeExpenseRecordDataSource = FakeExpenseRecordDataSource()
        viewModel = ExpenseAddViewModel(fakeExpenseRecordDataSource, Date())
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
    fun checkFormat() {
        Assert.assertEquals(
            true,
            viewModel.checkFormat(
                price = 100,
                accountName = "AccountName-1",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 0,
                accountName = "AccountName-1",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 100,
                accountName = "",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 100,
                accountName = "AccountName-1",
                categoryId = -1
            )
        )
    }
}