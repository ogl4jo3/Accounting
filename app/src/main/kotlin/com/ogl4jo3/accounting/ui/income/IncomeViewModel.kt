package com.ogl4jo3.accounting.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.IncomeRecordItem
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch
import java.util.Date

class IncomeViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val incomeRecordDataSource: IncomeRecordDataSource,
    incomeDate: Date?
) : ViewModel() {

    val date = MutableLiveData(incomeDate ?: Date())
    private val dateObserver = Observer<Date> {
        updateIncomeRecords(it)
    }

    private val _incomeRecords: MutableLiveData<List<IncomeRecordItem>> =
        MutableLiveData(emptyList())
    val incomeRecords: LiveData<List<IncomeRecordItem>> = _incomeRecords
    private val incomeRecordsObserver = Observer<List<IncomeRecordItem>> { incomeRecords ->
        _totalAmount.value = incomeRecords.sumOf { it.price }
    }

    private val _totalAmount: MutableLiveData<Int> = MutableLiveData(0)
    val totalAmount: LiveData<Int> = _totalAmount

    init {
        date.observeForever(dateObserver)
        incomeRecords.observeForever(incomeRecordsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        date.removeObserver(dateObserver)
        incomeRecords.removeObserver(incomeRecordsObserver)
    }

    private fun updateIncomeRecords(date: Date) = viewModelScope.launch {
        _incomeRecords.value = incomeRecordDataSource.getIncomeRecordsByDate(date)
            .mapNotNull {
                safeLet(
                    accountDataSource.getAccountById(it.accountId),
                    categoryDataSource.getCategoryById(it.categoryId)
                ) { account, category ->
                    IncomeRecordItem(
                        it.incomeRecordId,
                        it.price,
                        account,
                        category,
                        it.description,
                        it.recordTime
                    )
                }
            }
    }

    fun pickDate(time: Long) {
        date.value = Date(time)
    }

    fun switchToToday() {
        date.value = Date()
    }

}