package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.ExpenseRecord

class DefaultExpenseRecordDataSource(
    val expenseRecordDao: ExpenseRecordDao
) : ExpenseRecordDataSource {
    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecordDao.insertExpenseRecord(expenseRecord)
    }
}