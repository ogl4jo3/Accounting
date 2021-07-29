package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.data.IncomeRecord
import java.util.Date

class DefaultIncomeRecordDataSource(
    private val incomeRecordDao: IncomeRecordDao
) : IncomeRecordDataSource {
    override suspend fun insertIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecordDao.insertIncomeRecord(incomeRecord)
    }

    override suspend fun getIncomeRecordsByDate(date: Date): List<IncomeRecord> {
        return incomeRecordDao.getIncomeRecordsByDate(date.beginOfDay.time, date.endOfDay.time)
    }

    override suspend fun getIncomeRecordsByMonth(date: Date): List<IncomeRecord> {
        return incomeRecordDao.getIncomeRecordsByDate(
            date.beginOfMonth.time, date.endOfMonth.time
        )
    }

    override suspend fun getIncomeRecordsByYear(date: Date): List<IncomeRecord> {
        return incomeRecordDao.getIncomeRecordsByDate(
            date.beginOfYear.time, date.endOfYear.time
        )
    }

    override suspend fun updateIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecordDao.updateIncomeRecord(incomeRecord)
    }

    override suspend fun deleteIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecordDao.deleteIncomeRecord(incomeRecord)
    }
}