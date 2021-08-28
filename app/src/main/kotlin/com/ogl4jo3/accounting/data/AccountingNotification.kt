package com.ogl4jo3.accounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "notification",
    indices = [Index(value = ["time"], unique = true)]
)
data class AccountingNotification(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "time") var time: String,//"HH:MM". 00:00~23:59
    @ColumnInfo(name = "isOn") var isOn: Boolean,//notification status: on(true), off(false)
)
