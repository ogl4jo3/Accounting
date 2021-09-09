package com.ogl4jo3.accounting.common

import java.text.ParseException
import java.util.Date

val String.simpleDate: Date?
    get() = try {
        simpleDateFormat.parse(this)
    } catch (e: ParseException) {
        null
    }
