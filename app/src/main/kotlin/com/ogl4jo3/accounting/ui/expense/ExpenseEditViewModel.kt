package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch

class ExpenseEditViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val expenseRecordDataSource: ExpenseRecordDataSource,
    val expenseRecord: ExpenseRecord
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allExpenseCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allExpenseCategories: LiveData<List<Category>> = _allExpenseCategories

    val price = MutableLiveData(expenseRecord.price)
    val account = MutableLiveData<Account>()
    val category = MutableLiveData<Category>()
    val description = MutableLiveData(expenseRecord.description)

    var moneyInputError: () -> Unit = { }
    var navToExpenseFragment: () -> Unit = { }

    init {
        viewModelScope.launch {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allExpenseCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Expense)
            account.value = allAccounts.value?.find { it.id == expenseRecord.accountId }
            category.value = allExpenseCategories.value?.find { it.id == expenseRecord.categoryId }
        }
    }

    fun saveExpenseRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            expenseRecord.price = price
            expenseRecord.accountId = account.id
            expenseRecord.categoryId = category.id
            expenseRecord.description = description
            expenseRecord
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
            expenseRecordDataSource.deleteExpenseRecord(expenseRecord)
            navToExpenseFragment()
        }
    }

}