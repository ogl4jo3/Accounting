package com.ogl4jo3.accounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ogl4jo3.accounting.setting.accountmanagement.Account
import com.ogl4jo3.accounting.setting.categorymanagement.Category
import java.util.*

@Entity(tableName = "expense")
data class Expense(
        @PrimaryKey @ColumnInfo(name = "expenseId") var expenseId: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "price") var price: Int,
        @ColumnInfo(name = "category") var category: Category,
        @ColumnInfo(name = "account") var account: Account,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "recordTime") var recordTime: Date
)
