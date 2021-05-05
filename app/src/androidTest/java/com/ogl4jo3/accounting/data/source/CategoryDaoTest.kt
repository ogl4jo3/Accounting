package com.ogl4jo3.accounting.data.source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    private lateinit var database: AppDatabase

//    @Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(), AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `測試-新增類別和透過ID取得類別`() = runBlocking {
        Timber.d("測試-新增類別")
        val category = Category(
                orderNumber = 0,
                categoryName = "test",
                iconResId = 123,
                categoryType = CategoryType.Expense
        )
        database.categoryDao().insertCategory(category)
        Timber.d("category: $category")

        database.categoryDao().getCategoryById(category.categoryId)?.let { categoryLoaded ->
            Timber.d("categoryLoaded: $categoryLoaded")
            Assert.assertEquals(categoryLoaded.orderNumber, (category.orderNumber))
            Assert.assertEquals(categoryLoaded.categoryName, (category.categoryName))
            Assert.assertEquals(categoryLoaded.iconResId, (category.iconResId))
            Assert.assertEquals(categoryLoaded.categoryType, (category.categoryType))
        } ?: Assert.fail()
    }

    @Test
    fun `測試-取得不存在的類別`() = runBlocking {
        Timber.d("測試-取得不存在的類別")
        val category = database.categoryDao().getCategoryById("00")
        Timber.d("category: $category")
        Assert.assertNull(category)
    }

    @Test
    fun `測試-更新類別和透過ID取得類別`() = runBlocking {
        Timber.d("測試-新增類別")
        val category = Category(
                orderNumber = 0,
                categoryName = "test",
                iconResId = 123,
                categoryType = CategoryType.Expense
        )
        database.categoryDao().insertCategory(category)
        Timber.d("category: $category")

        category.orderNumber = 1
        category.categoryName = "updatedName"
        category.iconResId = 456
        category.categoryType = CategoryType.Income
        database.categoryDao().updateCategory(category)
        Timber.d("updated category: $category")

        database.categoryDao().getCategoryById(category.categoryId)?.let { categoryLoaded ->
            Timber.d("categoryLoaded: $categoryLoaded")
            Assert.assertEquals(categoryLoaded.orderNumber, (category.orderNumber))
            Assert.assertEquals(categoryLoaded.categoryName, (category.categoryName))
            Assert.assertEquals(categoryLoaded.iconResId, (category.iconResId))
            Assert.assertEquals(categoryLoaded.categoryType, (category.categoryType))
        } ?: Assert.fail()
    }

    @Test
    fun `測試-刪除類別`() = runBlocking {
        Timber.d("測試-刪除類別")
        val category = Category(
                orderNumber = 0,
                categoryName = "test",
                iconResId = 123,
                categoryType = CategoryType.Expense
        )
        database.categoryDao().insertCategory(category)
        Assert.assertNotNull(database.categoryDao().getCategoryById(category.categoryId))

        database.categoryDao().deleteCategory(category)
        Assert.assertNull(database.categoryDao().getCategoryById(category.categoryId))
    }

    @Test
    fun `測試-取得所有支出類別`() = runBlocking {
        Timber.d("測試-取得所有支出類別")
        val categories = arrayOf(
                Category(
                        orderNumber = 0,
                        categoryName = "test1",
                        iconResId = 123,
                        categoryType = CategoryType.Expense
                ),
                Category(
                        orderNumber = 2,
                        categoryName = "test2",
                        iconResId = 234,
                        categoryType = CategoryType.Expense
                ),
                Category(
                        orderNumber = 3,
                        categoryName = "test3",
                        iconResId = 345,
                        categoryType = CategoryType.Expense
                )
        )
        database.categoryDao().insertCategory(*categories)
        Assert.assertEquals(
                3, database.categoryDao().getCategoriesByType(CategoryType.Expense).size
        )
    }

    @Test
    fun `測試-取得所有收入類別`() = runBlocking {
        Timber.d("測試-取得所有收入類別")
        val categories = arrayOf(
                Category(
                        orderNumber = 0,
                        categoryName = "test1",
                        iconResId = 123,
                        categoryType = CategoryType.Income
                ),
                Category(
                        orderNumber = 2,
                        categoryName = "test2",
                        iconResId = 234,
                        categoryType = CategoryType.Income
                )
        )
        database.categoryDao().insertCategory(*categories)
        Assert.assertEquals(
                2, database.categoryDao().getCategoriesByType(CategoryType.Income).size
        )
    }

    @Test
    fun test() = runBlocking {
        Timber.d("test")
        println("test")
        val loaded = database.categoryDao().getCategoryById("123")
        println("loaded: $loaded")

//        Assert.assertEquals(loaded.orderNumber, (category.orderNumber))
//        Assert.assertEquals(loaded.categoryName, (category.categoryName))
//        Assert.assertEquals(loaded.iconResId, (category.iconResId))
//        Assert.assertEquals(loaded.categoryType, (category.categoryType))
    }
}