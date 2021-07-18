package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.MutableLiveData
import com.ogl4jo3.accounting.data.Category

interface CategoryAddViewModel {
    val selectedCategoryIcon: MutableLiveData<CategoryIcon>
    val categoryName: MutableLiveData<String>
    fun selectCategoryIcon(categoryIcon: CategoryIcon)
    fun addCategory()
    suspend fun addCategory(category: Category)
}