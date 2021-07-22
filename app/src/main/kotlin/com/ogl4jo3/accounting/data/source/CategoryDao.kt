package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category): Long

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryById(id: String): Category?

    @Query("SELECT * FROM category WHERE categoryType = :categoryType ORDER BY orderNumber ASC")
    suspend fun getCategoriesByType(categoryType: CategoryType): List<Category>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT COUNT(id) FROM category WHERE categoryType = :categoryType")
    suspend fun getNumberOfCategories(categoryType: CategoryType): Int

    @Query("SELECT COUNT(id) FROM category WHERE name = :name")
    suspend fun getNumberOfCategoriesByName(name: String): Int

    @Query("SELECT COUNT(id) FROM category WHERE name = :name AND id != :excludeId")
    suspend fun getNumberOfCategoriesByName(name: String, excludeId: String): Int

    @Query("SELECT MAX(orderNumber) FROM category WHERE categoryType = :categoryType")
    suspend fun getMaxOrderNumber(categoryType: CategoryType): Int?


}