package com.ogl4jo3.accounting.data.source

import androidx.room.*
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(vararg category: Category)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM category WHERE categoryId = :id")
    suspend fun getCategoryById(id: String): Category?

    @Delete
    suspend fun deleteCategory(category: Category)

    //更新類別排序編號,TODO:確認是否必要

    @Query("SELECT * FROM category WHERE categoryType = :categoryType")
    suspend fun getCategoriesByType(categoryType: CategoryType): List<Category>

    //檢查是否重複,TODO:確認是否必要


}