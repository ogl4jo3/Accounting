package com.ogl4jo3.accounting.data

import java.util.Date
import java.util.UUID

data class ExpenseRecordItem(
    var expenseRecordId: String = UUID.randomUUID().toString(),
    var price: Int,
    var account: Account,
    var category: Category,
    var description: String,
    var recordTime: Date
)
