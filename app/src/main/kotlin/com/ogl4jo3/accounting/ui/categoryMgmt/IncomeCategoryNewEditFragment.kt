package com.ogl4jo3.accounting.ui.categoryMgmt

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.categorymanagement.Category
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.keyboard.KeyboardUtil
import java.util.*

class IncomeCategoryNewEditFragment : Fragment() {

    private val args: IncomeCategoryNewEditFragmentArgs by navArgs()

    // UI元件
    private var ivCategoryIcon: ImageView? = null
    private var etCategoryName: EditText? = null
    private var gridViewIcon: GridView? = null
    private var btnSave: Button? = null
    private var btnDel: Button? = null
    private var category: Category? = null //所選擇的類別
    private lateinit var iconArray: IntArray //所有類別 Icon
    private var iconAmount = 0 //Icon數量
    private var mapData //GridView 顯示資料
            : MutableList<Map<String, Any?>>? = null
    private val mapIconKey = "icon" //GridView 資料對應KEY
    private var gridViewAdapter: SimpleAdapter? = null
    private var categoryIcon = 0
    private var categoryID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category_new_edit, container, false)
        initUI(view)
        setGridViewIcon() //設置GridView相關
        if (args.categoryJsonStr.isNullOrEmpty()) {  //新增
            ivCategoryIcon!!.setImageResource(R.drawable.ic_category_other)
            categoryIcon = R.drawable.ic_category_other
            categoryID = -1
            btnDel!!.visibility = View.GONE //新增時隱藏刪除按鈕
        } else {     //編輯
            ivCategoryIcon!!.setImageResource(category!!.icon)
            categoryIcon = category!!.icon
            categoryID = category!!.id
            etCategoryName!!.setText(category!!.name)
        }
        setOnClickListener()
        return view
    }

    /**
     * 初始化資料
     */
    private fun initData() {
        category = Gson().fromJson(args.categoryJsonStr, Category::class.java)
        setIconArray() //將IconArray從Resources取出並設置
        setGridViewData() //設置GridViewAdapter所需資料
    }

    /**
     * 初始化元件
     *
     * @param view View
     */
    private fun initUI(view: View) {
        ivCategoryIcon = view.findViewById<View>(R.id.iv_icon) as ImageView
        etCategoryName = view.findViewById<View>(R.id.et_name) as EditText
        btnSave = view.findViewById<View>(R.id.btn_new) as Button
        btnDel = view.findViewById<View>(R.id.btn_del) as Button
        gridViewIcon = view.findViewById<View>(R.id.gv_category_icon) as GridView
    }

    /**
     * 設置GridView
     */
    private fun setGridViewIcon() {
        gridViewAdapter = SimpleAdapter(
            this.activity,
            mapData,
            R.layout.item_category_icon,
            arrayOf(mapIconKey),
            intArrayOf(R.id.iv_icon)
        )
        //gridViewIcon.setNumColumns(5);
        gridViewIcon!!.adapter = gridViewAdapter
        gridViewIcon!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            ivCategoryIcon!!.setImageResource(iconArray[position])
            categoryIcon = iconArray[position]
            KeyboardUtil.closeKeyboard(activity)
            //Toast.makeText(getActivity(), "你選擇了" + position, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 設置GridViewAdapter所需資料
     */
    private fun setGridViewData() {
        mapData = ArrayList()
        for (i in 0 until iconAmount) {
            val data: MutableMap<String, Any?> = HashMap()
            data[mapIconKey] = iconArray[i]
            (mapData as ArrayList<Map<String, Any?>>).add(data)
        }
    }

    /**
     * 將IconArray從Resources取出並設置
     */
    private fun setIconArray() {
        val res = resources
        val icons = res.obtainTypedArray(R.array.category_icon)
        iconAmount = icons.length()
        iconArray = IntArray(iconAmount)
        for (i in 0 until iconAmount) {
            iconArray[i] = icons.getResourceId(i, -1)
            //Log.d(TAG, "iconArray[i] " + iconArray[i]);
        }
        // recycle the array
        icons.recycle()
    }

    /**
     * 儲存、刪除
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        btnSave!!.setOnClickListener(View.OnClickListener {
            val db = MyDBHelper.getDatabase(activity)
            val categoryName = etCategoryName!!.text.toString()
            if (categoryName.isEmpty()) {
                Toast.makeText(
                    activity, getString(
                        R.string.hint_input_category_name
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else if (CategoryDAO(db)
                    .checkIncomeRepeated(categoryName, categoryIcon, categoryID)
            ) { //檢查是否重複
                Toast.makeText(
                    activity, getString(
                        R.string.hint_duplicated_category
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val categoryNew = Category()
            categoryNew.icon = categoryIcon
            categoryNew.name = categoryName
            if (args.categoryJsonStr.isNullOrEmpty()) {  //新增
                CategoryDAO(db).newIncomeData(categoryNew)
            } else {
                categoryNew.id = category!!.id
                categoryNew.orderNum = category!!.orderNum
                CategoryDAO(db).saveIncomeData(categoryNew)
            }
            findNavController().popBackStack()
        })
        btnDel!!.setOnClickListener { //刪除
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(
                R.string.msg_category_del_confirm
            )
            builder.setMessage(
                R.string.msg_category_del_confirm_hint
            )
            builder.setPositiveButton(
                R.string.btn_del
            ) { _, _ ->
                val db = MyDBHelper.getDatabase(activity)
                CategoryDAO(db).deleteIncomeData(category)
                Toast.makeText(
                    activity, getString(
                        R.string.msg_category_deleted,
                        category!!.name
                    ), Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
            builder.setNegativeButton(
                R.string.btn_cancel
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.create().show()
        }
    }

}