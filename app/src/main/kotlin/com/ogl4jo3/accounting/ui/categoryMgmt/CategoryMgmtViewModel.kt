package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.LiveData
import com.ogl4jo3.accounting.data.Category

interface CategoryMgmtViewModel {
    val allCategories: LiveData<List<Category>>
    fun updateAllCategories()
    fun navigateToItem(category: Category)
    fun swapCategoryOrderNumber(fromCategory: Category, toCategory: Category)
    fun deleteCategory(category: Category, onSuccess: () -> Unit = {}, onFail: () -> Unit = {})
}