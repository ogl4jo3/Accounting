package com.ogl4jo3.accounting.data.local

import android.content.SharedPreferences
import com.ogl4jo3.accounting.data.local.SharedPreferencesHelper.Companion.PREFS_KEY_IS_FIRST_USE

class DefaultSharedPreferencesHelper(
    private val prefs: SharedPreferences
) : SharedPreferencesHelper {

    override var isFirstUse: Boolean
        get() = prefs.getBoolean(PREFS_KEY_IS_FIRST_USE, true)
        set(value) {
            prefs.edit().putBoolean(PREFS_KEY_IS_FIRST_USE, value).apply()
        }

}