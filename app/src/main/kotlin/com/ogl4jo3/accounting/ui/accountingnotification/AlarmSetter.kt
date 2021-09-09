package com.ogl4jo3.accounting.ui.accountingnotification

interface AlarmSetter {
    fun setInexactRepeating(hour: Int, minute: Int)
    fun cancel()
}