package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import java.util.Date

class ExpenseAddViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val expenseRecordDataSource: ExpenseRecordDataSource,
    val date: Date
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allExpenseCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allExpenseCategories: LiveData<List<Category>> = _allExpenseCategories

    val price = MutableLiveData<Int>()
    val account = MutableLiveData<Account>()
    val category = MutableLiveData<Category>()
    val description = MutableLiveData("")

    var moneyInputError: () -> Unit = { }
    var navToExpenseFragment: () -> Unit = { }

    init {
        runBlocking {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allExpenseCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Expense)
            account.value = allAccounts.value?.get(0)
            category.value = allExpenseCategories.value?.get(0)
        }
    }

    fun addExpenseRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            ExpenseRecord(
                price = price,
                accountId = account.id,
                categoryId = category.id,
                description = description,
                recordTime = date
            )
        }?.let { expenseRecord ->
            runBlocking { addExpenseRecord(expenseRecord) }
        } ?: return
    }

    suspend fun addExpenseRecord(expenseRecord: ExpenseRecord) {
        if (expenseRecord.price <= 0) {
            moneyInputError()
            return
        } else {
            expenseRecordDataSource.insertExpenseRecord(expenseRecord)
            navToExpenseFragment()
        }
    }
}