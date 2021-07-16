package com.ogl4jo3.accounting.ui.common.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.apply {
        val view = currentFocus ?: View(this@hideKeyboard)
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}