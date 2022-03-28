package com.ogl4jo3.accounting.ui.common

import androidx.databinding.InverseMethod
import timber.log.Timber

object Converter {
    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(value: Int): String {
        return value.toString()
    }

    @JvmStatic
    fun stringToInt(value: String): Int {
        return if (value.isEmpty()) {
            0
        } else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                Timber.e("NumberFormatException: $e")
                0
            }
        }
    }

    @InverseMethod("stringToPrice")
    @JvmStatic
    fun priceToString(value: Int?): String {
        return if (value == null || value == 0) "" else value.toString()
    }

    @JvmStatic
    fun stringToPrice(value: String): Int? {
        return if (value.isEmpty() || value == "0") {
            null
        } else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                Timber.e("NumberFormatException: $e")
                null
            }
        }
    }
}