package com.ogl4jo3.accounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "category", indices = [Index(value = ["name"], unique = true)])
data class Category(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "orderNumber") var orderNumber: Int = 0, //排序編號
    @ColumnInfo(name = "iconResName") var iconResName: String,// resources.getResourceEntryName/getIdentifier
    @ColumnInfo(name = "categoryType") var categoryType: CategoryType,//TODO: Converter??
)

enum class CategoryType {
    Expense, Income
}