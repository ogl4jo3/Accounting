package com.ogl4jo3.accounting.ui.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import com.ogl4jo3.accounting.data.source.FakeExpenseRecordDataSource
import com.ogl4jo3.accounting.testExpenseCategories
import com.ogl4jo3.accounting.testExpenseRecords
import com.ogl4jo3.accounting.ui.statistics.expense.ExpenseStatisticsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExpenseStatisticsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExpenseStatisticsViewModel

    @Before
    fun setup() {
        viewModel = ExpenseStatisticsViewModel(
            FakeCategoryDataSource(testExpenseCategories.toMutableList()),
            FakeExpenseRecordDataSource(testExpenseRecords.toMutableList()),
        )
    }

    @Test
    fun `test getStatisticsItemList`() {
        val statisticsItemList = viewModel.getStatisticsItemList(testExpenseRecords)

        println(statisticsItemList)

        Assert.assertEquals("2", statisticsItemList[0].categoryId)
        Assert.assertEquals(0, statisticsItemList[0].orderNumber)
        Assert.assertEquals(4300, statisticsItemList[0].amount)
        Assert.assertEquals(955, (statisticsItemList[0].percent * 1000).toInt())
        Assert.assertEquals(1, statisticsItemList[1].orderNumber)
        Assert.assertEquals(2, statisticsItemList[2].orderNumber)
    }

}