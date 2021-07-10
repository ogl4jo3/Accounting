package com.ogl4jo3.accounting.ui.categoryMgmt

import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.FakeCategoryDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class CategoryMgmtViewModelTest {

    //    private lateinit var accountAddViewModel: AccountAddViewModel
    private lateinit var fakeCategoryDataSource: FakeCategoryDataSource

    @Before
    fun setupViewModel() {
        fakeCategoryDataSource = FakeCategoryDataSource()
//        accountAddViewModel = AccountAddViewModel(fakeCategoryDataSource)
    }

    @Test
    fun `test add category`() = runBlocking {
        val accountsSize = fakeCategoryDataSource.categories.size
        addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(accountsSize + 1, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add two category`() = runBlocking {
        val accountsSize = fakeCategoryDataSource.categories.size
        addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Expense
            )
        )
        addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(accountsSize + 2, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add one expense category and one income category`() = runBlocking {
        val accountsSize = fakeCategoryDataSource.categories.size
        addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Income
            )
        )
        addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(accountsSize + 2, fakeCategoryDataSource.categories.size)
    }

    @Test
    fun `test add two expense category and two income category`() = runBlocking {
        val accountsSize = fakeCategoryDataSource.categories.size
        addCategory(
            Category(
                name = "test",
                iconResName = "TestIconResName",
                categoryType = CategoryType.Income
            )
        )
        addCategory(
            Category(
                name = "test3",
                iconResName = "Test3IconResName",
                categoryType = CategoryType.Income
            )
        )
        addCategory(
            Category(
                name = "test2",
                iconResName = "Test2IconResName",
                categoryType = CategoryType.Expense
            )
        )
        addCategory(
            Category(
                name = "test4",
                iconResName = "Test4IconResName",
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(accountsSize + 4, fakeCategoryDataSource.categories.size)
    }

    suspend fun addCategory(category: Category) {
        if (category.name.isBlank()) {
            //TODO: failed
            return
        } else if (fakeCategoryDataSource.hasDuplicatedName(category.name)) {
            //TODO: failed
            return
        } else {
            val id = fakeCategoryDataSource.insertCategory(category)
            if (id < 0) {
                Timber.e("insertCategory failed, category: $category")
            } else {
                //TODO: succeed
            }

        }
    }
}