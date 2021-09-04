package com.ogl4jo3.accounting.ui.accountingnotification

import java.util.Calendar

interface AlarmSetter {
    fun setInexactRepeating( calendar: Calendar)
    fun cancel()
}