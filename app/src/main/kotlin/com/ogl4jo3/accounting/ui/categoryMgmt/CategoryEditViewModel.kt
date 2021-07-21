package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class CategoryEditViewModel(
    private val categoryDataSource: CategoryDataSource,
    val categoryIcon: CategoryIcon,
    val category: Category,
) : ViewModel() {

    val selectedCategoryIcon = MutableLiveData(categoryIcon)
    val categoryName = MutableLiveData(category.name)

    var nameEmptyError: () -> Unit = { }
    var nameExistError: () -> Unit = { }
    var navToCategoryMgmtFragment: () -> Unit = { }

    fun selectCategoryIcon(categoryIcon: CategoryIcon) {
        selectedCategoryIcon.value = categoryIcon
    }

    fun saveCategory() {
        safeLet(selectedCategoryIcon.value, categoryName.value) { categoryIcon, categoryName ->
            category.name = categoryName
            category.iconResName = categoryIcon.iconEntryName
            category
        }?.let { category ->
            runBlocking { saveCategory(category) }
        } ?: run {
            Timber.e("Something error")
            navToCategoryMgmtFragment()
        }
    }

    suspend fun saveCategory(category: Category) {
        if (category.name.isBlank()) {
            nameEmptyError()
            return
        } else if (categoryDataSource.hasDuplicatedName(category.name, category.id)) {
            nameExistError()
            return
        } else {
            categoryDataSource.updateCategory(category)
            navToCategoryMgmtFragment()
        }
    }

    fun deleteCategory(onSuccess: () -> Unit = {}, onFail: () -> Unit = {}) {
        runBlocking {
            if (categoryDataSource.getNumberOfCategories(category.categoryType) <= 1) {
                onFail()
            } else {
                categoryDataSource.deleteCategory(category)
                onSuccess()
            }
        }
    }
}