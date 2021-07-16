package com.ogl4jo3.accounting.ui.categoryMgmt

import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CategoryMgmtViewModelTest {

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

    @Test
    fun `test add one category then update name`() = runBlocking {
        val categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        val category = fakeCategoryDataSource.getCategory(0)
        category.name = "test1234"
        saveCategory(category)
        Assert.assertEquals(categoriesSize + 1, fakeCategoryDataSource.categories.size)
        Assert.assertEquals("test1234", fakeCategoryDataSource.getCategory(0).name)
        Assert.assertEquals("TestIconResName", fakeCategoryDataSource.getCategory(0).iconResName)
        Assert.assertEquals(
            CategoryType.Expense,
            fakeCategoryDataSource.getCategory(0).categoryType
        )
        Assert.assertEquals(0, fakeCategoryDataSource.getCategory(0).orderNumber)
    }

    @Test
    fun `test save category failed because of duplicated name`() = runBlocking {
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
        val category = fakeCategoryDataSource.getCategory(0)
        category.name = "test2"
        saveCategory(category)
        Assert.assertEquals("test", fakeCategoryDataSource.getCategory(0).name)
    }

    suspend fun saveCategory(category: Category) {
        if (category.name.isBlank()) {
            //TODO: failed
            return
        } else if (fakeCategoryDataSource.hasDuplicatedName(category.name, category.id)) {
            //TODO: failed
            return
        } else {
            val id = fakeCategoryDataSource.updateCategory(category)
            //TODO: succeed
        }
    }

    @Test
    fun `test delete category`() = runBlocking {
        var categoriesSize = fakeCategoryDataSource.categories.size
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
        categoriesSize = fakeCategoryDataSource.categories.size
        val category = fakeCategoryDataSource.getCategory(0)
        deleteCategory(category)
        Assert.assertEquals(categoriesSize - 1, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test delete category failed because size == 1`() = runBlocking {
        var categoriesSize = fakeCategoryDataSource.categories.size
        expenseCategoryAddViewModel.addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(categoriesSize + 1, fakeCategoryDataSource.categories.size)
        categoriesSize = fakeCategoryDataSource.categories.size
        val category = fakeCategoryDataSource.getCategory(0)
        deleteCategory(category)
        Assert.assertEquals(categoriesSize, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test delete category failed because CategoryType == Expense and size == 1`() =
        runBlocking {
            var categoriesSize = fakeCategoryDataSource.categories.size
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
                    categoryType = CategoryType.Income
                )
            )
            Assert.assertEquals(categoriesSize + 2, fakeCategoryDataSource.categories.size)
            categoriesSize = fakeCategoryDataSource.categories.size
            val category = fakeCategoryDataSource.getCategory(0)
            deleteCategory(category)
            Assert.assertEquals(categoriesSize, fakeCategoryDataSource.categories.size)
        }

    suspend fun deleteCategory(category: Category) {
        if (fakeCategoryDataSource.getNumberOfCategories(category.categoryType) <= 1) {
            //TODO: failed
        } else {
            fakeCategoryDataSource.deleteCategory(category)
            //TODO: succeed
        }
    }
}