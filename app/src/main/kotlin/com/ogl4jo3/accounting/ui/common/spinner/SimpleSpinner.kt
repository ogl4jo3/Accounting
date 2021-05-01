package com.ogl4jo3.accounting.ui.common.spinner

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R

class SimpleSpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    private val view = View.inflate(context, R.layout.custom_spinner, this) as SimpleSpinner
    private var tvName: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>

    private var hasDefaultValue = false


    init {
        tvName = view.findViewById<AutoCompleteTextView>(R.id.tv_name).apply {
            inputType = InputType.TYPE_NULL
        }
    }

    /**
     * if hasDefaultValue == true, 將列表第一個值設為預設選擇
     * 此function需在[setAdapter]之前設定
     */
    fun setHasDefaultValue(hasDefaultValue: Boolean) {
        this.hasDefaultValue = hasDefaultValue
    }

    fun setAdapter(items: List<String>) {
        adapter = ArrayAdapter(context, R.layout.item_simple_spinner, items)
        tvName.setAdapter(adapter)
        if (items.isNotEmpty() && hasDefaultValue) tvName.setText(items[0], false)
    }

    fun getName(): String {
        return tvName.text.toString()
    }


}