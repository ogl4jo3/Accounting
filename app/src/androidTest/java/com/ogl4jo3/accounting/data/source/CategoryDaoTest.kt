package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }
    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        categoryDao = database.categoryDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-InsertCategory_And_GetCategoryById`() = runBlocking {
        Timber.d("Test-InsertCategory_And_GetCategoryById")
        val category = Category(
            orderNumber = 0,
            name = "test",
            iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_afternoon_tea),
            categoryType = CategoryType.Expense
        )
        categoryDao.insertCategory(category)
        Timber.d("category: $category")

        categoryDao.getCategoryById(category.id)?.let { categoryLoaded ->
            Timber.d("categoryLoaded: $categoryLoaded")
            Assert.assertEquals(categoryLoaded.orderNumber, (category.orderNumber))
            Assert.assertEquals(categoryLoaded.name, (category.name))
            Assert.assertEquals(categoryLoaded.iconResName, (category.iconResName))
            Assert.assertEquals(categoryLoaded.categoryType, (category.categoryType))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-GetNullCategory`() = runBlocking {
        Timber.d("Test-GetNullCategory")
        val category = categoryDao.getCategoryById("00")
        Timber.d("category: $category")
        Assert.assertNull(category)
    }

    @Test
    fun `Test-UpdateCategory_And_GetCategoryById`() = runBlocking {
        Timber.d("Test-UpdateCategory_And_GetCategoryById")
        val category = Category(
            orderNumber = 0,
            name = "test",
            iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_afternoon_tea),
            categoryType = CategoryType.Expense
        )
        categoryDao.insertCategory(category)
        Timber.d("category: $category")

        category.orderNumber = 1
        category.name = "updatedName"
        category.iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_gift)
        category.categoryType = CategoryType.Income
        categoryDao.updateCategory(category)
        Timber.d("updated category: $category")

        categoryDao.getCategoryById(category.id)?.let { categoryLoaded ->
            Timber.d("categoryLoaded: $categoryLoaded")
            Assert.assertEquals(categoryLoaded.orderNumber, (category.orderNumber))
            Assert.assertEquals(categoryLoaded.name, (category.name))
            Assert.assertEquals(categoryLoaded.iconResName, (category.iconResName))
            Assert.assertEquals(categoryLoaded.categoryType, (category.categoryType))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-DeleteCategory`() = runBlocking {
        Timber.d("Test-DeleteCategory")
        val category = Category(
            orderNumber = 0,
            name = "test",
            iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_afternoon_tea),
            categoryType = CategoryType.Expense
        )
        categoryDao.insertCategory(category)
        Assert.assertNotNull(categoryDao.getCategoryById(category.id))

        categoryDao.deleteCategory(category)
        Assert.assertNull(categoryDao.getCategoryById(category.id))
    }

    @Test
    fun `Test-GetAllExpenseCategory`() = runBlocking {
        Timber.d("Test-GetAllExpenseCategory")
        categoryDao.insertCategory(
            Category(
                orderNumber = 0,
                name = "test1",
                iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_afternoon_tea),
                categoryType = CategoryType.Expense
            )
        )
        categoryDao.insertCategory(
            Category(
                orderNumber = 2,
                name = "test2",
                iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_dinner),
                categoryType = CategoryType.Expense
            )
        )
        categoryDao.insertCategory(
            Category(
                orderNumber = 3,
                name = "test3",
                iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_lunch),
                categoryType = CategoryType.Expense
            )
        )
        Assert.assertEquals(3, categoryDao.getCategoriesByType(CategoryType.Expense).size)
    }

    @Test
    fun `Test-GetAllIncomeCategory`() = runBlocking {
        Timber.d("Test-GetAllIncomeCategory")
        categoryDao.insertCategory(
            Category(
                orderNumber = 0,
                name = "test1",
                iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_afternoon_tea),
                categoryType = CategoryType.Income
            )
        )
        categoryDao.insertCategory(
            Category(
                orderNumber = 2,
                name = "test2",
                iconResName = context.resources.getResourceEntryName(R.drawable.ic_category_dinner),
                categoryType = CategoryType.Income
            )
        )
        Assert.assertEquals(2, categoryDao.getCategoriesByType(CategoryType.Income).size)
    }

}