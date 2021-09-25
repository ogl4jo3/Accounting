package com.ogl4jo3.accounting.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch
import java.util.Date

class IncomeAddViewModel(
    private val accountDataSource: AccountDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val incomeRecordDataSource: IncomeRecordDataSource,
    val date: Date
) : ViewModel() {

    private val _allAccounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val allAccounts: LiveData<List<Account>> = _allAccounts

    private val _allIncomeCategories: MutableLiveData<List<Category>> =
        MutableLiveData(emptyList())
    val allIncomeCategories: LiveData<List<Category>> = _allIncomeCategories

    val price = MutableLiveData<Int>()
    val account = MutableLiveData<Account>()
    val category = MutableLiveData<Category>()
    val description = MutableLiveData("")

    var moneyInputError: () -> Unit = { }
    var navToIncomeFragment: () -> Unit = { }

    init {
        viewModelScope.launch {
            _allAccounts.value = accountDataSource.getAllAccounts()
            _allIncomeCategories.value =
                categoryDataSource.getCategoriesByType(CategoryType.Income)
            account.value = allAccounts.value?.find { it.isDefaultAccount }
            category.value = allIncomeCategories.value?.get(0)
        }
    }

    fun addIncomeRecord() {
        safeLet(
            price.value, account.value, category.value, description.value
        ) { price, account, category, description ->
            IncomeRecord(
                price = price,
                accountId = account.id,
                categoryId = category.id,
                description = description,
                recordTime = date
            )
        }?.let { incomeRecord ->
            viewModelScope.launch {
                addIncomeRecord(incomeRecord)
            }
        } ?: run {
            // check all necessary field
            if (price.value == null) {
                moneyInputError()
            }
            return
        }
    }

    suspend fun addIncomeRecord(incomeRecord: IncomeRecord) {
        if (incomeRecord.price <= 0) {
            moneyInputError()
            return
        } else {
            incomeRecordDataSource.insertIncomeRecord(incomeRecord)
            navToIncomeFragment()
        }
    }
}