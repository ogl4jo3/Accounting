package com.ogl4jo3.accounting.ui.common.spinner

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.SimpleAdapter
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.categorymanagement.Category
import timber.log.Timber
import java.util.ArrayList
import java.util.HashMap

class CategorySpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val mapNameKey = "name" //adapter 資料對應KEY
    private val mapIconKey = "icon" //adapter 資料對應KEY

    private val view = View.inflate(context, R.layout.custom_spinner, this) as CategorySpinner
    private var tvCategory: AutoCompleteTextView =
        view.findViewById<AutoCompleteTextView>(R.id.tv_name).apply {
            inputType = InputType.TYPE_NULL
        }
    private lateinit var categoryAdapter: SimpleAdapter

    private var items: List<Category> = emptyList()
    private var selectedItem: Category? = null

    fun setAdapter(categoryList: List<Category>) {
        items = categoryList
        val categoriesLen: Int = items.size
        val mapData = ArrayList<Map<String, Any>>()
        for (i in 0 until categoriesLen) {
            val data: MutableMap<String, Any> = HashMap()
            data[mapNameKey] = items[i].name
            data[mapIconKey] = items[i].icon
            mapData.add(data)
        }
        categoryAdapter = SimpleAdapter(
            context,
            mapData,
            R.layout.item_category_drop_down,
            arrayOf(mapNameKey, mapIconKey),
            intArrayOf(R.id.tv_category_name, R.id.iv_category_icon)
        )
        tvCategory.setAdapter(categoryAdapter)
        tvCategory.onItemClickListener = OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick: ${items[i].name}, ID: ${items[i].id}, icon: ${items[i].icon}")
            selectedItem = items[i]

            //TODO: show icon
//            tvCategory.setCompoundDrawablesWithIntrinsicBounds(
//                    ContextCompat.getDrawable(context, items[i].icon), null, null, null)
            tvCategory.setText(items[i].name, false)
        }
    }

    fun getSelectedItem(): Category? {
        //TODO: set Category NonNull
        return selectedItem
    }

}