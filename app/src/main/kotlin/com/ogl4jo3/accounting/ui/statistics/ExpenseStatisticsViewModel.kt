package com.ogl4jo3.accounting.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.common.nextMonth
import com.ogl4jo3.accounting.common.nextYear
import com.ogl4jo3.accounting.common.previousMonth
import com.ogl4jo3.accounting.common.previousYear
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import kotlinx.coroutines.runBlocking
import java.util.Date


class ExpenseStatisticsViewModel(
    private val categoryDataSource: CategoryDataSource,
    private val expenseRecordDataSource: ExpenseRecordDataSource,
) : ViewModel() {

    val statisticsUnit = MutableLiveData(TabStatisticsUnit.MONTH)
    private val statisticsUnitObserver = Observer<TabStatisticsUnit?> {
        if (it == null) return@Observer
        date.value?.let { date ->
            updateStatisticsItemList(it, date)
        }
    }

    val date = MutableLiveData(Date())
    private val dateObserver = Observer<Date?> {
        if (it == null) return@Observer
        statisticsUnit.value?.let { statisticsUnit ->
            updateStatisticsItemList(statisticsUnit, it)
        }
    }

    private val _statisticsItemList: MutableLiveData<List<StatisticsItem>> =
        MutableLiveData(emptyList())
    val statisticsItemList: LiveData<List<StatisticsItem>> = _statisticsItemList

    init {
        date.observeForever(dateObserver)
        statisticsUnit.observeForever(statisticsUnitObserver)
    }

    override fun onCleared() {
        super.onCleared()
        date.removeObserver(dateObserver)
        statisticsUnit.removeObserver(statisticsUnitObserver)
    }

    fun previousDate() {
        statisticsUnit.value?.let { tabUnit ->
            date.value = when (tabUnit) {
                TabStatisticsUnit.MONTH -> date.value?.previousMonth
                TabStatisticsUnit.YEAR -> date.value?.previousYear
            }
        }
    }

    fun nextDate() {
        statisticsUnit.value?.let { statisticsUnit ->
            date.value = when (statisticsUnit) {
                TabStatisticsUnit.MONTH -> date.value?.nextMonth
                TabStatisticsUnit.YEAR -> date.value?.nextYear
            }
        }
    }

    fun updateStatisticsItemList(statisticsUnit: TabStatisticsUnit, date: Date) = runBlocking {
        val expenseRecords = when (statisticsUnit) {
            TabStatisticsUnit.MONTH -> expenseRecordDataSource.getExpenseRecordsByMonth(date)
            TabStatisticsUnit.YEAR -> expenseRecordDataSource.getExpenseRecordsByYear(date)
        }
        _statisticsItemList.value = getStatisticsItemList(expenseRecords)
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

    suspend fun getCategoryById(categoryId: String): Category? {
        return categoryDataSource.getCategoryById(categoryId)
    }
}

enum class TabStatisticsUnit {
    MONTH, YEAR;
}