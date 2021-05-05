package com.ogl4jo3.accounting.data

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "category", indices = [Index(value = ["categoryName"], unique = true)])
data class Category(
        @PrimaryKey @ColumnInfo(name = "categoryId")
        var categoryId: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "orderNumber") var orderNumber: Int, //排序編號
        @ColumnInfo(name = "categoryName") var categoryName: String,
        @ColumnInfo(name = "iconResId") @DrawableRes var iconResId: Int,
        @ColumnInfo(name = "categoryType") var categoryType: CategoryType,//TODO: Converter
)

enum class CategoryType(type: Int) {
    Expense(0),
    Income(1)
}