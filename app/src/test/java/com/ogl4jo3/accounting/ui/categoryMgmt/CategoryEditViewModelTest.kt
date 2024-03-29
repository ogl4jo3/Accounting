package com.ogl4jo3.accounting.ui.categoryMgmt

import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CategoryEditViewModelTest {

    private lateinit var categoryEditViewModel: CategoryEditViewModel
    private lateinit var fakeCategoryDataSource: FakeCategoryDataSource

    @Before
    fun setupViewModel() {
        val defaultCategories = mutableListOf(
            Category(
                name = "test",
                iconResName = "ic_category_other",
                categoryType = CategoryType.Expense,
                orderNumber = 0
            ),
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense,
                orderNumber = 1
            )
        )
        fakeCategoryDataSource = FakeCategoryDataSource(defaultCategories)
        categoryEditViewModel =
            CategoryEditViewModel(
                fakeCategoryDataSource,
                CategoryIcon(R.drawable.ic_category_other, "ic_category_other"),
                defaultCategories[0]
            )
    }

    @Test
    fun `test save category`() = runBlocking {
        val categories = fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)
        val category = categories[0]
        category.name = "test123"
        category.iconResName = "12345"
        categoryEditViewModel.saveCategory(category)
        Assert.assertEquals(
            "test123",
            fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)[0].name
        )
        Assert.assertEquals(
            "12345",
            fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)[0].iconResName
        )
    }

    @Test
    fun `test save duplicate name category`() = runBlocking {
        val categories = fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)
        val category = categories[0]
        category.name = categories[1].name
        categoryEditViewModel.saveCategory(category)
        Assert.assertEquals(
            categories[0].name,
            fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)[0].name
        )
    }

    @Test
    fun `test delete category`() = runBlocking {
        categoryEditViewModel.deleteCategory()
        Assert.assertEquals(1, fakeCategoryDataSource.getNumberOfCategories(CategoryType.Expense))
    }

    @Test
    fun `test delete category failed because have one category at least`() = runBlocking {
        val defaultCategories = mutableListOf(
            Category(
                name = "test",
                iconResName = "ic_category_other",
                categoryType = CategoryType.Expense
            )
        )
        fakeCategoryDataSource = FakeCategoryDataSource(defaultCategories)
        categoryEditViewModel.deleteCategory()
        Assert.assertEquals(1, fakeCategoryDataSource.getNumberOfCategories(CategoryType.Expense))
    }

    @Test
    fun `test delete category failed because have one category at least and type == expense`() =
        runBlocking {
            val defaultCategories = mutableListOf(
                Category(
                    name = "test",
                    iconResName = "ic_category_other",
                    categoryType = CategoryType.Expense
                ),
                Category(
                    name = "test123",
                    iconResName = "ic_category_other111",
                    categoryType = CategoryType.Income
                )
            )
            fakeCategoryDataSource = FakeCategoryDataSource(defaultCategories)
            categoryEditViewModel.deleteCategory()
            Assert.assertEquals(
                1,
                fakeCategoryDataSource.getNumberOfCategories(CategoryType.Expense)
            )
        }
}