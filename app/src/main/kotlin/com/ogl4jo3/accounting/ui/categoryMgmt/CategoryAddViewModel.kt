package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class CategoryAddViewModel(
    private val categoryDataSource: CategoryDataSource,
    val categoryType: CategoryType,
    defaultCategoryIcon: CategoryIcon,
) : ViewModel() {

    val selectedCategoryIcon = MutableLiveData(defaultCategoryIcon)
    val categoryName = MutableLiveData("")

    var nameEmptyError: () -> Unit = { }
    var nameExistError: () -> Unit = { }
    var navPopBackStack: () -> Unit = { }

    fun selectCategoryIcon(categoryIcon: CategoryIcon) {
        selectedCategoryIcon.value = categoryIcon
    }

    fun addCategory() {
        safeLet(selectedCategoryIcon.value, categoryName.value) { categoryIcon, categoryName ->
            Category(
                name = categoryName,
                iconResName = categoryIcon.iconEntryName,
                categoryType = categoryType
            )
        }?.let { account ->
            runBlocking { addCategory(account) }
        } ?: return
    }

    suspend fun addCategory(category: Category) {
        if (category.name.isBlank()) {
            nameEmptyError()
            return
        } else if (categoryDataSource.hasDuplicatedName(category.name)) {
            nameExistError()
            return
        } else {
            val id = categoryDataSource.insertCategory(category)
            if (id < 0) {
                Timber.e("insertCategory failed, category: $category")
            } else {
                navPopBackStack()
            }
        }
    }

}