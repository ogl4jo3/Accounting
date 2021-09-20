package com.ogl4jo3.accounting.ui.common.spinner

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.SimpleAdapter
import com.google.android.material.textfield.TextInputLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.ui.categoryMgmt.drawableId
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import timber.log.Timber

class CategorySpinner : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    private val view = View.inflate(context, R.layout.spinner_category, this) as CategorySpinner
    private var tvCategoryName: AutoCompleteTextView =
        view.findViewById<AutoCompleteTextView>(R.id.tv_category_name).apply {
            inputType = InputType.TYPE_NULL
        }

    private lateinit var categories: List<Category>
    private var selectedItem: Category? = null

    fun setAdapter(items: List<Category>) {
        categories = items
        val mapNameKey = "name"
        val mapIconKey = "icon"
        val mapData: List<Map<String, Any>> = items.map {
            mapOf(
                mapNameKey to it.name,
                mapIconKey to it.iconResName.drawableId(context.packageName, resources)
            )
        }
        tvCategoryName.setAdapter(
            SimpleAdapter(
                context,
                mapData,
                R.layout.item_category_spinner,
                arrayOf(mapNameKey, mapIconKey),
                intArrayOf(R.id.tv_category_name, R.id.iv_category_icon)
            )
        )
    }

    fun setOnItemClickListener(onChange: () -> Unit) {
        tvCategoryName.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Timber.d("onItemClick: ${categories[i].name}, ID: ${categories[i].id}, icon: ${categories[i].iconResName}")
            selectItem(categories[i])
            onChange()
        }
    }

    fun selectItem(item: Category) {
        selectedItem = item
        tvCategoryName.setText(item.name, false)
        //TODO: show icon
//            tvCategory.setCompoundDrawablesWithIntrinsicBounds(
//                    ContextCompat.getDrawable(context, items[i].icon), null, null, null)
    }

    fun getSelectedItem(): Category? {
        return selectedItem
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        this.hideKeyboard()
        return super.onInterceptTouchEvent(ev)
    }

}