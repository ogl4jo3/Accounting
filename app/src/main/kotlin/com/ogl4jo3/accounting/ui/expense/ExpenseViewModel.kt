package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.ExpenseRecordItem
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch
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

    private val _expenseRecords: MutableLiveData<List<ExpenseRecordItem>> =
        MutableLiveData(emptyList())
    val expenseRecords: LiveData<List<ExpenseRecordItem>> = _expenseRecords
    private val expenseRecordsObserver = Observer<List<ExpenseRecordItem>> { expenseRecords ->
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

    private fun updateExpenseRecords(date: Date) = viewModelScope.launch {
        _expenseRecords.value = expenseRecordDataSource.getExpenseRecordsByDate(date)
            .mapNotNull {
                safeLet(
                    accountDataSource.getAccountById(it.accountId),
                    categoryDataSource.getCategoryById(it.categoryId)
                ) { account, category ->
                    ExpenseRecordItem(
                        it.expenseRecordId,
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