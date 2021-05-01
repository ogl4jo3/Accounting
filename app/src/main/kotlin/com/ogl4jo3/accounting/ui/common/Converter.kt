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
}