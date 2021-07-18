package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.MutableLiveData
import com.ogl4jo3.accounting.data.Category

interface CategoryEditViewModel {
    val selectedCategoryIcon: MutableLiveData<CategoryIcon>
    val categoryName: MutableLiveData<String>
    fun selectCategoryIcon(categoryIcon: CategoryIcon)
    fun saveCategory()
    suspend fun saveCategory(category: Category)
    fun deleteCategory(onSuccess: () -> Unit = {}, onFail: () -> Unit = {})
}