package com.ogl4jo3.accounting.ui.statistics.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.common.nextMonth
import com.ogl4jo3.accounting.common.nextYear
import com.ogl4jo3.accounting.common.previousMonth
import com.ogl4jo3.accounting.common.previousYear
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import com.ogl4jo3.accounting.ui.common.PieChartData
import com.ogl4jo3.accounting.ui.statistics.IGetCategory
import com.ogl4jo3.accounting.ui.statistics.StatisticsItem
import com.ogl4jo3.accounting.ui.statistics.TabStatisticsUnit
import kotlinx.coroutines.runBlocking
import java.util.Date


class IncomeStatisticsViewModel(
    private val categoryDataSource: CategoryDataSource,
    private val incomeRecordDataSource: IncomeRecordDataSource,
) : ViewModel(), IGetCategory {

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

    private fun updateStatisticsItemList(statisticsUnit: TabStatisticsUnit, date: Date) = runBlocking {
        val incomeRecords = when (statisticsUnit) {
            TabStatisticsUnit.MONTH -> incomeRecordDataSource.getIncomeRecordsByMonth(date)
            TabStatisticsUnit.YEAR -> incomeRecordDataSource.getIncomeRecordsByYear(date)
        }
        _statisticsItemList.value = getStatisticsItemList(incomeRecords)
    }

    fun getStatisticsItemList(incomeRecords: List<IncomeRecord>): List<StatisticsItem> {
        val statisticsItemList = incomeRecords.groupBy { it.categoryId }
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

    override suspend fun getCategoryById(categoryId: String): Category? {
        return categoryDataSource.getCategoryById(categoryId)
    }

    fun getPieChartData(statisticsItemList: List<StatisticsItem>): List<PieChartData> =
        runBlocking {
            return@runBlocking statisticsItemList.map {
                PieChartData(
                    name = getCategoryById(it.categoryId)?.name ?: "",
                    value = it.amount
                )
            }
        }
}