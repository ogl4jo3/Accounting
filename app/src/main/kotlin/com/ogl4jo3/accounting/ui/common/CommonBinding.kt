package com.ogl4jo3.accounting.ui.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ogl4jo3.accounting.common.simpleDate
import com.ogl4jo3.accounting.common.simpleDateString
import com.ogl4jo3.accounting.ui.categoryMgmt.drawableId
import java.util.Date

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("dateText")
fun setDateText(textView: TextView, date: Date?) {
    textView.text = date?.simpleDateString
}

@InverseBindingAdapter(attribute = "dateText")
fun getDateText(textView: TextView): Date? {
    return textView.text.toString().simpleDate
}

@BindingAdapter("dateTextAttrChanged")
fun setListeners(textView: TextView, attrChange: InverseBindingListener) {
    textView.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            attrChange.onChange()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, image: Int) {
    if (image == -1) return
    imageView.setImageResource(image)
}

@BindingAdapter("imgRscByDrawableName")
fun setImgRscByDrawableName(imageView: ImageView, drawableName: String) {
    imageView.apply {
        setImageResource(drawableName.drawableId(context.packageName, resources))
    }
}