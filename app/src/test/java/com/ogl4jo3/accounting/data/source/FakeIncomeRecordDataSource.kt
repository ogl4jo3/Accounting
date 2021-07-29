package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.data.IncomeRecord
import java.util.Date

class FakeIncomeRecordDataSource(
    var incomeRecords: MutableList<IncomeRecord> = mutableListOf()
) : IncomeRecordDataSource {

    override suspend fun insertIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecords.add(incomeRecord)
    }

    override suspend fun getIncomeRecordsByDate(date: Date): List<IncomeRecord> {
        return incomeRecords.filter {
            it.recordTime.after(date.beginOfDay) && it.recordTime.before(date.endOfDay)
        }
    }

    override suspend fun getIncomeRecordsByMonth(date: Date): List<IncomeRecord> {
        return incomeRecords.filter {
            it.recordTime.after(date.beginOfMonth) && it.recordTime.before(date.endOfMonth)
        }
    }

    override suspend fun getIncomeRecordsByYear(date: Date): List<IncomeRecord> {
        return incomeRecords.filter {
            it.recordTime.after(date.beginOfYear) && it.recordTime.before(date.endOfYear)
        }
    }

    override suspend fun updateIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecords.replaceAll { if (it.incomeRecordId == incomeRecord.incomeRecordId) incomeRecord else it }
    }

    override suspend fun deleteIncomeRecord(incomeRecord: IncomeRecord) {
        incomeRecords.remove(incomeRecord)
    }

    fun getRecord(index: Int): IncomeRecord {
        return incomeRecords[index].copy()
    }

}