package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.DefaultCategoryDataSource
import kotlinx.coroutines.runBlocking

class ExpenseCategoryMgmtViewModel(
    private val categoryDataSource: CategoryDataSource = DefaultCategoryDataSource()
) : ViewModel() {

    private val _allCategories: MutableLiveData<List<Category>> = MutableLiveData(emptyList())
    val allCategories: LiveData<List<Category>> = _allCategories


    fun updateAllCategories() {
        _allCategories.value =
            runBlocking { categoryDataSource.getCategoriesByType(CategoryType.Expense) }
    }

    fun swapCategoryOrderNumber(fromCategory: Category, toCategory: Category) {
        runBlocking {
            categoryDataSource.swapCategoryOrderNumber(fromCategory, toCategory)
            updateAllCategories()
        }
    }

    fun deleteCategory(category: Category, onSuccess: () -> Unit = {}, onFail: () -> Unit = {}) {
        runBlocking {
            if (categoryDataSource.getNumberOfCategories(category.categoryType) <= 1) {
                onFail()
            } else {
                categoryDataSource.deleteCategory(category)
                updateAllCategories()
                onSuccess()
            }
        }
    }
}