package com.ogl4jo3.accounting.ui.accountMgmt

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.SimpleAdapter
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import timber.log.Timber

class AccountCategorySpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val view =
        View.inflate(context, R.layout.spinner_account_category, this) as AccountCategorySpinner
    private var tvAccountCategoryName: AutoCompleteTextView =
        view.findViewById<AutoCompleteTextView>(R.id.tv_account_category_name).apply {
            inputType = InputType.TYPE_NULL
        }
    private lateinit var categoryAdapter: SimpleAdapter

    private val items: Array<AccountCategory> = AccountCategory.values()
    private var selectedItem: AccountCategory? = null

    init {
        setAdapter()
    }

    private fun setAdapter() {
        val mapNameKey = "name"
        val mapIconKey = "icon"
        val mapData: List<Map<String, Any>> = items.map {
            mapOf(
                mapNameKey to context.getString(it.nameRes),
                mapIconKey to it.iconRes
            )
        }
        categoryAdapter = SimpleAdapter(
            context,
            mapData,
            R.layout.item_account_category_spinner,
            arrayOf(mapNameKey, mapIconKey),
            intArrayOf(R.id.tv_account_category_name, R.id.iv_account_category_icon)
        )
        tvAccountCategoryName.setAdapter(categoryAdapter)
    }

    fun setOnItemClickListener(onChange: () -> Unit) {
        tvAccountCategoryName.onItemClickListener = OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick name: ${context.getString(items[i].nameRes)}, iconRes: ${items[i].iconRes}")
            selectItem(items[i])
            onChange()
        }
    }

    fun selectItem(item: AccountCategory) {
        selectedItem = item
        tvAccountCategoryName.setText(context.getString(item.nameRes), false)
        //TODO: show icon
//            tvCategory.setCompoundDrawablesWithIntrinsicBounds(
//                    ContextCompat.getDrawable(context, items[i].icon), null, null, null)
    }

    fun getSelectedItem(): AccountCategory? {
        return selectedItem
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        this.hideKeyboard()
        return super.onInterceptTouchEvent(ev)
    }

}