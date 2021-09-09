package com.ogl4jo3.accounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "notification",
    indices = [Index(value = ["hour", "minute"], unique = true)]
)
data class AccountingNotification(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "hour") var hour: Int,// 0~23
    @ColumnInfo(name = "minute") var minute: Int,// 0~59
    @ColumnInfo(name = "isOn") var isOn: Boolean,//notification status: on(true), off(false)
) {
    fun is24HFormat(): Boolean {
        return (hour in 0..23 && minute in 0..59)
    }
}
