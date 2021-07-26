package com.ogl4jo3.accounting.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard

/**
 * 鍵盤相關工具
 * Created by ogl4jo3 on 2017/6/14.
 */
object KeyboardUtil {

    /**
     * 點按不是EditText的View時，隱藏鍵盤
     *
     * @param activity Activity
     * @param view     需要關閉鍵盤範圍的畫面
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(activity: Activity, view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                activity.hideKeyboard()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(activity, innerView)
            }
        }
    }
}