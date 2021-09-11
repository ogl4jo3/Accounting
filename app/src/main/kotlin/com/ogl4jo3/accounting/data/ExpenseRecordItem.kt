package com.ogl4jo3.accounting.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

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
