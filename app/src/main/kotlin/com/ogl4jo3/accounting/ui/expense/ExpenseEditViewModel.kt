package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.ExpenseRecordItem
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch

class ExpenseEditViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val expenseRecordDataSource: ExpenseRecordDataSource,
    val expenseRecordItem: ExpenseRecordItem
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allExpenseCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allExpenseCategories: LiveData<List<Category>> = _allExpenseCategories

    val price = MutableLiveData(expenseRecordItem.price)
    val account = MutableLiveData(expenseRecordItem.account)
    val category = MutableLiveData(expenseRecordItem.category)
    val description = MutableLiveData(expenseRecordItem.description)

    var moneyInputError: () -> Unit = { }
    var navToExpenseFragment: () -> Unit = { }

    init {
        viewModelScope.launch {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allExpenseCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Expense)
        }
    }

    fun saveExpenseRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            ExpenseRecord(
                expenseRecordItem.expenseRecordId,
                price,
                account.id,
                category.id,
                description,
                expenseRecordItem.recordTime
            )
        }?.let { expenseRecord ->
            viewModelScope.launch {
                saveExpenseRecord(expenseRecord)
            }
        } ?: run {
            // check all necessary field
            if (price.value == null) {
                moneyInputError()
            }
            return
        }
    }

    suspend fun saveExpenseRecord(expenseRecord: ExpenseRecord) {
        if (expenseRecord.price <= 0) {
            moneyInputError()
            return
        } else {
            expenseRecordDataSource.updateExpenseRecord(expenseRecord)
            navToExpenseFragment()
        }
    }

    fun deleteExpenseRecord() {
        viewModelScope.launch {
            expenseRecordDataSource.deleteExpenseRecord(expenseRecordItem.expenseRecord)
            navToExpenseFragment()
        }
    }

}