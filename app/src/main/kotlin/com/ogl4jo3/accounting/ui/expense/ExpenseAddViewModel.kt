package com.ogl4jo3.accounting.ui.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.common.expenses.Expenses
import com.ogl4jo3.accounting.common.simpleDateString
import com.ogl4jo3.accounting.utils.safeLet
import timber.log.Timber
import java.util.*

class ExpenseAddViewModel(
        val date: Date
) : ViewModel() {
    val price = MutableLiveData<Int>()
    val description = MutableLiveData<String>()

    var moneyInputError: () -> Unit = { }
    var accountInputError: () -> Unit = { }
    var categoryInputError: () -> Unit = { }
    var saveExpenseToDB: (expense: Expenses) -> Unit = { }

    fun saveExpenseRecord(accountName: String, categoryId: Int?) {
        Timber.d("money: ${price.value}, accountName: ${accountName}, categoryId: ${categoryId}, description: ${description.value}")
        if (checkFormat(price.value, accountName, categoryId)) {
            safeLet(price.value, categoryId) { price, categoryID ->
                val expenses = Expenses()
                expenses.price = price
                expenses.categoryId = categoryID
                expenses.accountName = accountName
                expenses.description = description.value
                expenses.recordTime = date.simpleDateString
                saveExpenseToDB(expenses)//TODO: refactor by repository
            }
        }
    }

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
}