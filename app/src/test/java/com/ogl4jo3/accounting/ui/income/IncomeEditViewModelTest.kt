package com.ogl4jo3.accounting.ui.income

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeIncomeRecordDataSource
import com.ogl4jo3.accounting.testAccounts
import com.ogl4jo3.accounting.testDateArray
import com.ogl4jo3.accounting.testIncomeCategories
import com.ogl4jo3.accounting.testIncomeRecordItem
import com.ogl4jo3.accounting.testIncomeRecords
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncomeEditViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: IncomeEditViewModel
    private lateinit var fakeIncomeRecordDataSource: FakeIncomeRecordDataSource

    @Before
    fun setup() {
        fakeIncomeRecordDataSource =
            FakeIncomeRecordDataSource(testIncomeRecords.toMutableList())
        viewModel = IncomeEditViewModel(
            FakeAccountDataSource(testAccounts.toMutableList()),
            FakeCategoryDataSource(testIncomeCategories.toMutableList()),
            fakeIncomeRecordDataSource, testIncomeRecordItem
        )
    }

    @Test
    fun `test save incomeRecord`() = runBlocking {
        val incomeRecord = fakeIncomeRecordDataSource.getRecord(0)
        incomeRecord.price = 999
        incomeRecord.accountId = testAccounts[1].id
        incomeRecord.categoryId = testIncomeCategories[2].id
        incomeRecord.recordTime = testDateArray[1]
        incomeRecord.description = "test1234"
        viewModel.saveIncomeRecord(incomeRecord)
        Assert.assertEquals(999, fakeIncomeRecordDataSource.incomeRecords[0].price)
        Assert.assertEquals(
            testAccounts[1].id, fakeIncomeRecordDataSource.incomeRecords[0].accountId
        )
        Assert.assertEquals(
            testIncomeCategories[2].id, fakeIncomeRecordDataSource.incomeRecords[0].categoryId
        )
        Assert.assertEquals(
            testDateArray[1], fakeIncomeRecordDataSource.incomeRecords[0].recordTime
        )
        Assert.assertEquals("test1234", fakeIncomeRecordDataSource.incomeRecords[0].description)
    }

    @Test
    fun `test save income record failed because price==0`() = runBlocking {
        val incomeRecord = fakeIncomeRecordDataSource.getRecord(0)
        incomeRecord.price = 0
        incomeRecord.description = "test1234"
        viewModel.saveIncomeRecord(incomeRecord)
        Assert.assertEquals(
            testIncomeRecords[0].price,
            fakeIncomeRecordDataSource.incomeRecords[0].price
        )
        Assert.assertEquals(
            testIncomeRecords[0].description,
            fakeIncomeRecordDataSource.incomeRecords[0].description
        )
    }

    @Test
    fun `test delete incomeRecord`() = runBlocking {
        val size = fakeIncomeRecordDataSource.incomeRecords.size
        viewModel.deleteIncomeRecord()
        Assert.assertEquals(size - 1, fakeIncomeRecordDataSource.incomeRecords.size)
    }

}