package com.ogl4jo3.accounting.ui.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeIncomeRecordDataSource
import com.ogl4jo3.accounting.testIncomeCategories
import com.ogl4jo3.accounting.testIncomeRecords
import com.ogl4jo3.accounting.ui.statistics.income.IncomeStatisticsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IncomeStatisticsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: IncomeStatisticsViewModel

    @Before
    fun setup() {
        viewModel = IncomeStatisticsViewModel(
            FakeCategoryDataSource(testIncomeCategories.toMutableList()),
            FakeIncomeRecordDataSource(testIncomeRecords.toMutableList()),
        )
    }

    @Test
    fun `test getStatisticsItemList`() {
        val statisticsItemList = viewModel.getStatisticsItemList(testIncomeRecords)

        println(statisticsItemList)

        Assert.assertEquals("2", statisticsItemList[0].categoryId)
        Assert.assertEquals(0, statisticsItemList[0].orderNumber)
        Assert.assertEquals(2400, statisticsItemList[0].amount)
        Assert.assertEquals(923, (statisticsItemList[0].percent * 1000).toInt())
        Assert.assertEquals(1, statisticsItemList[1].orderNumber)
        Assert.assertEquals(2, statisticsItemList[2].orderNumber)
    }

}