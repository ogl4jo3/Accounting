package com.ogl4jo3.accounting.ui.income

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeIncomeRecordDataSource
import com.ogl4jo3.accounting.testAccounts
import com.ogl4jo3.accounting.testIncomeCategories
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

class IncomeAddViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: IncomeAddViewModel
    private lateinit var fakeIncomeRecordDataSource: FakeIncomeRecordDataSource

    @Before
    fun setup() {
        fakeIncomeRecordDataSource = FakeIncomeRecordDataSource()
        viewModel = IncomeAddViewModel(
            FakeAccountDataSource(testAccounts.toMutableList()),
            FakeCategoryDataSource(testIncomeCategories.toMutableList()),
            fakeIncomeRecordDataSource,
            Date()
        )
    }

    @Test
    fun `test add income record`() = runBlocking {
        val incomeRecordSize = fakeIncomeRecordDataSource.incomeRecords.size
        viewModel.addIncomeRecord(
            IncomeRecord(
                price = 50,
                accountId = "0",
                categoryId = "0",
                description = "測試用收入",
                recordTime = viewModel.date
            )
        )
        Assert.assertEquals(incomeRecordSize + 1, fakeIncomeRecordDataSource.incomeRecords.size)
    }

    @Test
    fun `test add income record failed because price==0`() = runBlocking {
        val incomeRecordSize = fakeIncomeRecordDataSource.incomeRecords.size
        viewModel.addIncomeRecord(
            IncomeRecord(
                price = 0,
                accountId = "0",
                categoryId = "0",
                description = "測試用收入",
                recordTime = viewModel.date
            )
        )
        Assert.assertEquals(incomeRecordSize, fakeIncomeRecordDataSource.incomeRecords.size)
    }
}