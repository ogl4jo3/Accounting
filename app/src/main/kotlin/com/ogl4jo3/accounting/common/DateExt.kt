package com.ogl4jo3.accounting.common

import java.text.SimpleDateFormat
import java.util.*

val simpleDateFormat get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

val Date.simpleDateString: String get() = simpleDateFormat.format(this)
