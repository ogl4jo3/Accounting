package com.ogl4jo3.accounting.ui.statistics

import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.testExpenseRecords
import org.junit.Assert
import org.junit.Test

class ExpenseStatisticsViewModelTest {

    //TODO: add test
//    @Rule
//    @JvmField
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: ExpenseAddViewModel
//    private lateinit var fakeExpenseRecordDataSource: FakeExpenseRecordDataSource

//    @Before
//    fun setup() {
//        fakeExpenseRecordDataSource = FakeExpenseRecordDataSource()
//        viewModel = ExpenseAddViewModel(
//            FakeAccountDataSource(testAccounts.toMutableList()),
//            FakeCategoryDataSource(testExpenseCategories.toMutableList()),
//            fakeExpenseRecordDataSource,
//            Date()
//        )
//    }

    @Test
    fun `test getStatisticsItemList`() {

        val statisticsItemList = getStatisticsItemList(testExpenseRecords)


        println(statisticsItemList)

        Assert.assertEquals("2", statisticsItemList[0].categoryId)
        Assert.assertEquals(0, statisticsItemList[0].orderNumber)
        Assert.assertEquals(4300, statisticsItemList[0].amount)
        Assert.assertEquals(955, (statisticsItemList[0].percent*1000).toInt())
        Assert.assertEquals(1, statisticsItemList[1].orderNumber)
        Assert.assertEquals(2, statisticsItemList[2].orderNumber)
    }


    fun getStatisticsItemList(expenseRecords: List<ExpenseRecord>): List<StatisticsItem> {
        val statisticsItemList = expenseRecords.groupBy { it.categoryId }
            .map {
                StatisticsItem(
                    categoryId = it.key,
                    amount = it.value.sumOf { record -> record.price },
                    percent = 0.0f,
                    orderNumber = 0
                )
            }
            .sortedByDescending { it.amount }
        val sum = statisticsItemList.sumOf { it.amount }
        statisticsItemList.forEachIndexed { index, statisticsItem ->
            statisticsItem.orderNumber = index
            statisticsItem.percent = statisticsItem.amount.div(sum.toFloat())
        }
        return statisticsItemList
    }

}