package com.ogl4jo3.accounting.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.IncomeRecordItem
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch

class IncomeEditViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val incomeRecordDataSource: IncomeRecordDataSource,
    val incomeRecordItem: IncomeRecordItem
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allIncomeCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allIncomeCategories: LiveData<List<Category>> = _allIncomeCategories

    val price = MutableLiveData(incomeRecordItem.price)
    val account = MutableLiveData(incomeRecordItem.account)
    val category = MutableLiveData(incomeRecordItem.category)
    val description = MutableLiveData(incomeRecordItem.description)

    var moneyInputError: () -> Unit = { }
    var navToIncomeFragment: () -> Unit = { }

    init {
        viewModelScope.launch {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allIncomeCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Income)
        }
    }

    fun saveIncomeRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            IncomeRecord(
                incomeRecordItem.incomeRecordId,
                price,
                account.id,
                category.id,
                description,
                incomeRecordItem.recordTime
            )
        }?.let { incomeRecord ->
            viewModelScope.launch {
                saveIncomeRecord(incomeRecord)
            }
        } ?: run {
            // check all necessary field
            if (price.value == null) {
                moneyInputError()
            }
            return
        }
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
        viewModelScope.launch {
            incomeRecordDataSource.deleteIncomeRecord(incomeRecordItem.incomeRecord)
            navToIncomeFragment()
        }
    }

}