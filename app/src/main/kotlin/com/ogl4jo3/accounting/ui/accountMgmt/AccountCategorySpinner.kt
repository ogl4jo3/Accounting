package com.ogl4jo3.accounting.ui.accountMgmt

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.SimpleAdapter
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.AccountCategory
import timber.log.Timber
import java.util.*

class AccountCategorySpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val view =
        View.inflate(context, R.layout.custom_spinner, this) as AccountCategorySpinner
    private var tvCategory: AutoCompleteTextView =
        view.findViewById<AutoCompleteTextView>(R.id.tv_name).apply {
            inputType = InputType.TYPE_NULL
        }
    private lateinit var categoryAdapter: SimpleAdapter

    private val items: List<AccountCategory> =
        listOf(AccountCategory.Cash, AccountCategory.Card, AccountCategory.Bank)
    private var selectedItem: AccountCategory? = null

    init {
        setAdapter()
    }

    private fun setAdapter() {
        val categoriesLen: Int = items.size
        val mapData = ArrayList<Map<String, Any>>()
        for (i in 0 until categoriesLen) {
            val data: MutableMap<String, Any> = HashMap()
            data[KEY_MAP_NAME] = context.getString(items[i].nameRes)
            data[KEY_MAP_ICON] = items[i].iconRes
            mapData.add(data)
        }
        categoryAdapter = SimpleAdapter(
            context,
            mapData,
            R.layout.item_category_drop_down,
            arrayOf(KEY_MAP_NAME, KEY_MAP_ICON),
            intArrayOf(R.id.tv_category_name, R.id.iv_category_icon)
        )
        tvCategory.setAdapter(categoryAdapter)
    }

    fun setOnItemClickListener(onChange: () -> Unit) {
        tvCategory.onItemClickListener = OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick name: ${context.getString(items[i].nameRes)}, iconRes: ${items[i].iconRes}")
            selectItem(items[i])
            onChange()
        }

    }

    fun selectItem(item: AccountCategory) {
        selectedItem = item
        tvCategory.setText(context.getString(item.nameRes), false)
        //TODO: show icon
//            tvCategory.setCompoundDrawablesWithIntrinsicBounds(
//                    ContextCompat.getDrawable(context, items[i].icon), null, null, null)
    }

    fun getSelectedItem(): AccountCategory? {
        return selectedItem
    }

    companion object {
        private const val KEY_MAP_NAME = "name" //adapter 資料對應KEY
        private const val KEY_MAP_ICON = "icon" //adapter 資料對應KEY
    }
}