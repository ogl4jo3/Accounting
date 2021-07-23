package com.ogl4jo3.accounting.ui.common.spinner

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Account
import timber.log.Timber

class AccountSpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val view = View.inflate(context, R.layout.spinner_account, this) as AccountSpinner
    private var tvAccountName: AutoCompleteTextView =
        view.findViewById<AutoCompleteTextView>(R.id.tv_account_name).apply {
            inputType = InputType.TYPE_NULL
        }
    private lateinit var selectedItem: Account

    fun setAdapter(items: List<Account>) {
        val itemsName = items.map { it.name }
        tvAccountName.setAdapter(ArrayAdapter(context, R.layout.item_account_spinner, itemsName))
        tvAccountName.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick: ${items[i].name}, ID: ${items[i].id}")
            selectedItem = items[i]
        }
        if (items.isNotEmpty()) { // set items[0] as default item
            selectedItem = items[0]
            tvAccountName.setText(itemsName[0], false)
        }
    }

    fun getName(): String {
        return tvAccountName.text.toString()
    }

    fun getSelectedItem(): Account {
        return selectedItem
    }


}