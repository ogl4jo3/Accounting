package com.ogl4jo3.accounting.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import kotlinx.coroutines.runBlocking
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

    private val _incomeRecords: MutableLiveData<List<IncomeRecord>> = MutableLiveData(emptyList())
    val incomeRecords: LiveData<List<IncomeRecord>> = _incomeRecords
    private val incomeRecordsObserver = Observer<List<IncomeRecord>> { incomeRecords ->
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

    private fun updateIncomeRecords(date: Date) = runBlocking {
        _incomeRecords.value = incomeRecordDataSource.getIncomeRecordsByDate(date)
    }

    fun pickDate(time: Long) {
        date.value = Date(time)
    }

    fun switchToToday() {
        date.value = Date()
    }

    suspend fun getCategoryById(categoryId: String): Category? {
        return categoryDataSource.getCategoryById(categoryId)
    }

    suspend fun getAccountById(accountId: String): Account? {
        return accountDataSource.getAccountById(accountId)
    }

}