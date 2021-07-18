package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.DefaultCategoryDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class IncomeCategoryEditViewModel(
    private val categoryDataSource: CategoryDataSource = DefaultCategoryDataSource(),
    val categoryIcon: CategoryIcon,
    val category: Category,
) : ViewModel(), CategoryEditViewModel {

    override val selectedCategoryIcon = MutableLiveData(categoryIcon)
    override val categoryName = MutableLiveData(category.name)

    var nameEmptyError: () -> Unit = { }
    var nameExistError: () -> Unit = { }
    var navPopBackStack: () -> Unit = { }

    override fun selectCategoryIcon(categoryIcon: CategoryIcon) {
        selectedCategoryIcon.value = categoryIcon
    }

    override fun saveCategory() {
        safeLet(selectedCategoryIcon.value, categoryName.value) { categoryIcon, categoryName ->
            category.name = categoryName
            category.iconResName = categoryIcon.iconEntryName
            category
        }?.let { category ->
            runBlocking { saveCategory(category) }
        } ?: run {
            Timber.e("Something error")
            navPopBackStack()
        }
    }

    override suspend fun saveCategory(category: Category) {
        if (category.name.isBlank()) {
            nameEmptyError()
            return
        } else if (categoryDataSource.hasDuplicatedName(category.name, category.id)) {
            nameExistError()
            return
        } else {
            categoryDataSource.updateCategory(category)
            navPopBackStack()
        }
    }

    override fun deleteCategory(onSuccess: () -> Unit, onFail: () -> Unit) {
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