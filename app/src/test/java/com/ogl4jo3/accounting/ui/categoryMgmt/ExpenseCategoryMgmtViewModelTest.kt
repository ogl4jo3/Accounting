package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExpenseCategoryMgmtViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var expenseCategoryMgmtViewModel: ExpenseCategoryMgmtViewModel
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
        expenseCategoryMgmtViewModel = ExpenseCategoryMgmtViewModel(fakeCategoryDataSource)
    }

    @Test
    fun `test update category order number`() = runBlocking {
        expenseCategoryMgmtViewModel.swapCategoryOrderNumber(
            fakeCategoryDataSource.getCategory(0),
            fakeCategoryDataSource.getCategory(1)
        )
        val category1 = fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)[0]
        val category2 = fakeCategoryDataSource.getCategoriesByType(CategoryType.Expense)[1]
        Assert.assertEquals(0, category1.orderNumber)
        Assert.assertEquals("test2", category1.name)
        Assert.assertEquals("Test2IconResName", category1.iconResName)
        Assert.assertEquals(1, category2.orderNumber)
        Assert.assertEquals("test", category2.name)
        Assert.assertEquals("ic_category_other", category2.iconResName)
    }

}