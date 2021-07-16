package com.ogl4jo3.accounting.ui.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.apply {
        hideSoftInputFromWindow(windowToken, 0)
    }
}