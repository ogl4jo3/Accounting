package com.ogl4jo3.accounting.data.local

interface SharedPreferencesHelper {

    companion object {
        const val PREFS_KEY_IS_FIRST_USE = "PREFS_KEY_IS_FIRST_USE"
    }

    var isFirstUse: Boolean
}