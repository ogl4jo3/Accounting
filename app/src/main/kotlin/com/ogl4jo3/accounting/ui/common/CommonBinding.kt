package com.ogl4jo3.accounting.ui.common

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ogl4jo3.accounting.common.simpleDateString
import java.util.*

@BindingAdapter("dateText")
fun setDateText(textView: TextView, date: Date) {
    textView.text = date.simpleDateString
}