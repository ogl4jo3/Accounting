package com.ogl4jo3.accounting.ui.categoryMgmt

import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ExpenseCategoryAddViewModelTest {

    private lateinit var expenseCategoryAddViewModel: ExpenseCategoryAddViewModel
    private lateinit var fakeCategoryDataSource: FakeCategoryDataSource

    @Before
    fun setupViewModel() {
        fakeCategoryDataSource = FakeCategoryDataSource()
        expenseCategoryAddViewModel = ExpenseCategoryAddViewModel(
            fakeCategoryDataSource,
            CategoryIcon(R.drawable.ic_category_other, "ic_category_other")
        )
    }

    @Test
    fun `test add category`() = runBlocking {
        val categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(categoriesSize + 1, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add two category`() = runBlocking {
        val categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(categoriesSize + 2, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add one expense category and one income category`() = runBlocking {
        val categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Income
            )
        )
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(categoriesSize + 2, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add two expense category and two income category`() = runBlocking {
        val categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Income
            )
        )
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test3",
                iconResName = "Test3IconResName",
                categoryType = CategoryType.Income
            )
        )
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test4",
                iconResName = "Test4IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(categoriesSize + 4, fakeCategoryDataSource.categories.size)
    }

}