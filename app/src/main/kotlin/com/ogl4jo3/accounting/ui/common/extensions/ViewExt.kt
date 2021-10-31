package com.ogl4jo3.accounting.ui.common.extensions

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.ogl4jo3.accounting.ui.common.OnSingleClickListener

fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?)?.apply {
        toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?)?.apply {
        hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.setOnSingleClickListener(onClick: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(onClick))
}

fun View.setOnSingleClickListener(onClick: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(onClick))
}

fun View.focusAndShowKeyboard() {
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?)?.apply {
                    showSoftInput(this@showTheKeyboardNow, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        // wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}