package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.IncomeRecord
import java.util.Date

interface IncomeRecordDataSource {
    suspend fun insertIncomeRecord(incomeRecord: IncomeRecord)
    suspend fun getIncomeRecordsByDate(date: Date): List<IncomeRecord>
    suspend fun updateIncomeRecord(incomeRecord: IncomeRecord)
    suspend fun deleteIncomeRecord(incomeRecord: IncomeRecord)

}