package com.ogl4jo3.accounting.ui.common

import androidx.databinding.InverseMethod

object Converter {
    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(value: Int): String {
        return value.toString()
    }

    @JvmStatic
    fun stringToInt(value: String): Int {
        return if (value.isEmpty()) 0 else value.toInt()
    }

    @InverseMethod("stringToPrice")
    @JvmStatic
    fun priceToString(value: Int?): String {
        return if (value == null || value == 0) "" else value.toString()
    }

    @JvmStatic
    fun stringToPrice(value: String): Int? {
        return if (value.isEmpty() || value == "0") null else value.toInt()
    }
}