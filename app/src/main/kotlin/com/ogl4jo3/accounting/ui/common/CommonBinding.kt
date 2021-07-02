package com.ogl4jo3.accounting.ui.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ogl4jo3.accounting.common.simpleDateString
import java.util.Date

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("dateText")
fun setDateText(textView: TextView, date: Date) {
    textView.text = date.simpleDateString
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, image: Int) {
    if (image == -1) return
    imageView.setImageResource(image)
}