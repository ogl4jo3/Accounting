package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import kotlinx.coroutines.runBlocking
import java.util.Date

class ExpenseViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val expenseRecordDataSource: ExpenseRecordDataSource,
    expenseDate: Date?
) : ViewModel() {

    val date = MutableLiveData(expenseDate ?: Date())
    private val dateObserver = Observer<Date> {
        updateExpenseRecords(it)
    }

    private val _expenseRecords: MutableLiveData<List<ExpenseRecord>> = MutableLiveData(emptyList())
    val expenseRecords: LiveData<List<ExpenseRecord>> = _expenseRecords
    private val expenseRecordsObserver = Observer<List<ExpenseRecord>> { expenseRecords ->
        _totalAmount.value = expenseRecords.sumOf { it.price }
    }

    private val _totalAmount: MutableLiveData<Int> = MutableLiveData(0)
    val totalAmount: LiveData<Int> = _totalAmount

    init {
        date.observeForever(dateObserver)
        expenseRecords.observeForever(expenseRecordsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        date.removeObserver(dateObserver)
        expenseRecords.removeObserver(expenseRecordsObserver)
    }

    fun updateExpenseRecords(date: Date) = runBlocking {
        _expenseRecords.value = expenseRecordDataSource.getExpenseRecordsByDate(date)
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