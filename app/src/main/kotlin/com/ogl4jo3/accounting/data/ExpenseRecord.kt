package com.ogl4jo3.accounting.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "expenseRecord",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = CASCADE
        ),
    ]
)
@Parcelize
data class ExpenseRecord(
    @PrimaryKey @ColumnInfo(name = "expenseRecordId") var expenseRecordId: String = UUID.randomUUID()
        .toString(),
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "accountId") var accountId: String,
    @ColumnInfo(name = "categoryId") var categoryId: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "recordTime") var recordTime: Date
) : Parcelable

@Parcelize
data class ExpenseRecordItem(
    val expenseRecordId: String = UUID.randomUUID().toString(),
    val price: Int,
    val account: Account,
    val category: Category,
    val description: String,
    val recordTime: Date
) : Parcelable {
    val expenseRecord
        get() = ExpenseRecord(
            expenseRecordId, price,
            account.id, category.id,
            description, recordTime
        )
}
