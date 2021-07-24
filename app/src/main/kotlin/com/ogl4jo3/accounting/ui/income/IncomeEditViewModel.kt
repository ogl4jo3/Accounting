package com.ogl4jo3.accounting.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking

class IncomeEditViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val incomeRecordDataSource: IncomeRecordDataSource,
    val incomeRecord: IncomeRecord
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allIncomeCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allIncomeCategories: LiveData<List<Category>> = _allIncomeCategories

    val price = MutableLiveData(incomeRecord.price)
    val account = MutableLiveData<Account>()
    val category = MutableLiveData<Category>()
    val description = MutableLiveData(incomeRecord.description)

    var moneyInputError: () -> Unit = { }
    var navToIncomeFragment: () -> Unit = { }

    init {
        runBlocking {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allIncomeCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Income)
            account.value = allAccounts.value?.find { it.id == incomeRecord.accountId }
            category.value = allIncomeCategories.value?.find { it.id == incomeRecord.categoryId }
        }
    }

    fun saveIncomeRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            incomeRecord.price = price
            incomeRecord.accountId = account.id
            incomeRecord.categoryId = category.id
            incomeRecord.description = description
            incomeRecord
        }?.let { incomeRecord ->
            runBlocking { saveIncomeRecord(incomeRecord) }
        } ?: return
    }

    suspend fun saveIncomeRecord(incomeRecord: IncomeRecord) {
        if (incomeRecord.price <= 0) {
            moneyInputError()
            return
        } else {
            incomeRecordDataSource.updateIncomeRecord(incomeRecord)
            navToIncomeFragment()
        }
    }

    fun deleteIncomeRecord() {
        runBlocking {
            incomeRecordDataSource.deleteIncomeRecord(incomeRecord)
            navToIncomeFragment()
        }
    }

}