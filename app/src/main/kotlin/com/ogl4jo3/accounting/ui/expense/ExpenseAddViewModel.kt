package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.common.expenses.Expenses
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
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

    fun updateAllAccounts() = runBlocking {
        _allAccounts.value = accountDataSource.getAllAccounts()
    }

    fun updateAllCategories() = runBlocking {
        _allExpenseCategories.value = categoryDataSource.getCategoriesByType(CategoryType.Expense)
    }


    val price = MutableLiveData<Int>()
    val description = MutableLiveData<String>()

    var moneyInputError: () -> Unit = { }
    var accountInputError: () -> Unit = { }
    var categoryInputError: () -> Unit = { }
    var saveExpenseToDB: (expense: Expenses) -> Unit = { }

    //TODO:
//    fun saveExpenseRecord(accountName: String, categoryId: Int?) {
//        Timber.d("money: ${price.value}, accountName: ${accountName}, categoryId: ${categoryId}, description: ${description.value}")
//        if (checkFormat(price.value, accountName, categoryId)) {
//            safeLet(price.value, categoryId) { price, categoryID ->
//                val expenses = Expenses()
//                expenses.price = price
//                expenses.categoryId = categoryID
//                expenses.accountName = accountName
//                expenses.description = description.value
//                expenses.recordTime = date.simpleDateString
//                saveExpenseToDB(expenses)//TODO: refactor by repository
//            }
//        }
//    }

    fun checkFormat(price: Int?, accountName: String, categoryId: Int?): Boolean {
        var isSuccessful = true
        if (price == null || price <= 0) {
            moneyInputError()
            isSuccessful = false
        } else if (accountName.isEmpty()) {
            accountInputError()
            isSuccessful = false
        } else if (categoryId == null || categoryId == -1) {
            categoryInputError()
            isSuccessful = false
        }
        return isSuccessful
    }

    suspend fun addExpenseRecord(expenseRecord: ExpenseRecord) {
        if (expenseRecord.price <= 0) {
            moneyInputError()
            return
        } else {
            expenseRecordDataSource.insertExpenseRecord(expenseRecord)
        }
    }
}