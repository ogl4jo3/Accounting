package com.ogl4jo3.accounting.ui.income

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.income.Income
import com.ogl4jo3.accounting.common.income.IncomeDAO
import com.ogl4jo3.accounting.setting.accountmanagement.Account
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.dialog.CategoryDialogFragment
import com.ogl4jo3.accounting.utils.dialog.CategoryDialogFragment.onCategoryListener
import com.ogl4jo3.accounting.utils.string.StringUtil
import java.util.*

class IncomeNewEditFragment : Fragment(), onCategoryListener {

    private val args: IncomeNewEditFragmentArgs by navArgs()

    //UI元件
    private var tvDate //日期
            : TextView? = null
    private var etMoney //金額
            : EditText? = null
    private var llCategory //類別
            : LinearLayout? = null
    private var ivCategoryIcon //類別圖示
            : ImageView? = null
    private var tvCategoryName //類別名稱
            : TextView? = null
    private var llAccount //帳戶
            : LinearLayout? = null
    private var tvAccountName //帳戶名稱
            : TextView? = null
    private var llStableIncome //固定收入
            : LinearLayout? = null
    private var etDescription //描述
            : EditText? = null
    private var btnNew //新增按鈕
            : Button? = null
    private var categoryId = -1 //類別ID  未選擇時為-1
    private var account: Account? = null

    //編輯時用
    private var income: Income? = null
    private var btnSave //儲存按鈕
            : Button? = null
    private var btnDel //刪除按鈕
            : Button? = null
    private var dateStr: String? = null
    private var incomeJson //如果incomeJson為空或null代表是新增，否則為編輯
            : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sqLiteDatabase = MyDBHelper.getDatabase(activity)
        account = AccountDAO(sqLiteDatabase).defaultAccount
        dateStr = args.dateStr
        args.incomeId?.let {
            income = IncomeDAO(sqLiteDatabase).getById(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_income_new_edit, container, false)
        initUI(view)
        setViewData()
        setOnClickListener()
        return view
    }

    /**
     * 初始化元件
     *
     * @param view View
     */
    private fun initUI(view: View) {
        tvDate = view.findViewById<View>(R.id.tv_date) as TextView
        etMoney = view.findViewById<View>(R.id.et_money) as EditText
        llCategory = view.findViewById<View>(R.id.ll_category) as LinearLayout
        ivCategoryIcon = view.findViewById<View>(R.id.iv_category_icon) as ImageView
        tvCategoryName = view.findViewById<View>(R.id.tv_category_name) as TextView
        llAccount = view.findViewById<View>(R.id.ll_account) as LinearLayout
        tvAccountName = view.findViewById<View>(R.id.tv_account_name) as TextView
        llStableIncome = view.findViewById<View>(R.id.ll_stable_income) as LinearLayout
        etDescription = view.findViewById<View>(R.id.et_description) as EditText
        btnNew = view.findViewById<View>(R.id.btn_new) as Button
        //編輯時用
        btnSave = view.findViewById<View>(R.id.btn_save) as Button
        btnDel = view.findViewById<View>(R.id.btn_del) as Button
    }

    /**
     * 設置元件資料
     */
    private fun setViewData() {
        tvDate!!.text = dateStr
        if (income != null) { //    編輯時
            btnNew!!.visibility = View.GONE
            btnSave!!.visibility = View.VISIBLE
            btnDel!!.visibility = View.VISIBLE
            etMoney!!.setText(income!!.price.toString())
            categoryId = income!!.categoryId
            val db = MyDBHelper.getDatabase(activity)
            val category = CategoryDAO(db).getIncomeData(categoryId)
            ivCategoryIcon!!.setImageResource(category.icon)
            tvCategoryName!!.text = category.name
            account = AccountDAO(db).getAccountByName(income!!.accountName)
            etDescription!!.setText(income!!.description)
        }
        tvAccountName!!.text = account!!.name
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        llCategory!!.setOnClickListener {
            val categoryDialogFragment = CategoryDialogFragment.newInstance(CategoryDialogFragment.INCOME)
            categoryDialogFragment.setTargetFragment(this@IncomeNewEditFragment, 0)
            categoryDialogFragment.show(requireFragmentManager(), null)
        }
        btnNew!!.setOnClickListener(View.OnClickListener {
            val priceStr = etMoney!!.text.toString()
            val description = etDescription!!.text.toString()
            if (priceStr.isEmpty() && categoryId == -1) {
                Toast.makeText(activity, R.string.msg_input_money_category_account,
                        Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (priceStr.isEmpty()) {
                Toast.makeText(activity, R.string.msg_input_money, Toast.LENGTH_SHORT)
                        .show()
                return@OnClickListener
            } else if (categoryId == -1) {
                Toast.makeText(activity, R.string.msg_input_category, Toast.LENGTH_SHORT)
                        .show()
                return@OnClickListener
            } else if (StringUtil.isNullorEmpty(account!!.name)) {
                Toast.makeText(activity, R.string.msg_input_account, Toast.LENGTH_SHORT)
                        .show()
                return@OnClickListener
            }
            val price = Integer.valueOf(priceStr)
            val income = Income()
            income.price = price
            income.categoryId = categoryId
            income.accountName = account!!.name
            income.description = description
            income.recordTime = dateStr
            val db = MyDBHelper.getDatabase(activity)
            IncomeDAO(db).newIncomeData(income)
            val fragmentManager = fragmentManager
            fragmentManager!!.popBackStack()
        })
        llAccount!!.setOnClickListener {
            val db = MyDBHelper.getDatabase(activity)
            val accountList = AccountDAO(db).all
            val accountsName: MutableList<String> = ArrayList()
            for (account in accountList) {
                accountsName.add(account.name)
            }
            AlertDialog.Builder(activity).setTitle(R.string.msg_choose_account)
                    .setItems(accountsName.toTypedArray()
                    ) { dialog, position ->
                        val accountName = accountsName[position]
                        tvAccountName!!.text = accountName
                        account = accountList[position]
                    }.show()
        }
        llStableIncome!!.setOnClickListener { //TODO:
            Toast.makeText(activity, getString(R.string.msg_todo), Toast.LENGTH_SHORT)
                    .show()
        }
        //編輯用
        btnSave!!.setOnClickListener(View.OnClickListener {
            val priceStr = etMoney!!.text.toString()
            val description = etDescription!!.text.toString()
            if (priceStr.isEmpty()) {
                Toast.makeText(activity, R.string.msg_input_money, Toast.LENGTH_SHORT)
                        .show()
                return@OnClickListener
            }
            val price = Integer.valueOf(priceStr)
            income!!.price = price
            income!!.categoryId = categoryId
            income!!.accountName = account!!.name
            income!!.description = description
            val db = MyDBHelper.getDatabase(activity)
            IncomeDAO(db).saveIncomeData(income)
            val fragmentManager = fragmentManager
            fragmentManager!!.popBackStack()
        })
        btnDel!!.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.msg_income_del_confirm)
            builder.setPositiveButton(R.string.btn_del) { dialogInterface, i ->
                val db = MyDBHelper.getDatabase(activity)
                IncomeDAO(db).delIncomeData(income!!.id)
                val fragmentManager = fragmentManager
                fragmentManager!!.popBackStack()
                dialogInterface.dismiss()
            }
            builder.setNegativeButton(R.string.btn_cancel
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            builder.create().show()
        }
    }

    override fun chooseCategory(categoryID: Int) {
        //Toast.makeText(getActivity(), "CategoryID:" + categoryID, Toast.LENGTH_SHORT).show();
        categoryId = categoryID
        val db = MyDBHelper.getDatabase(activity)
        val categoryTmp = CategoryDAO(db).getIncomeData(categoryID)
        ivCategoryIcon!!.setImageResource(categoryTmp.icon)
        tvCategoryName!!.text = categoryTmp.name
    }
}