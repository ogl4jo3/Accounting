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

    private lateinit var accounts: List<Account>
    private var selectedItem: Account? = null

    fun setAdapter(items: List<Account>) {
        accounts = items
        val itemsName = items.map { it.name }
        tvAccountName.setAdapter(ArrayAdapter(context, R.layout.item_account_spinner, itemsName))
    }

    fun setOnItemClickListener(onChange: () -> Unit) {
        tvAccountName.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick: ${accounts[i].name}, ID: ${accounts[i].id}")
            selectItem(accounts[i])
            onChange()
        }
    }

    fun selectItem(item: Account) {
        selectedItem = item
        tvAccountName.setText(item.name, false)
    }

    fun getSelectedItem(): Account? {
        return selectedItem
    }

}