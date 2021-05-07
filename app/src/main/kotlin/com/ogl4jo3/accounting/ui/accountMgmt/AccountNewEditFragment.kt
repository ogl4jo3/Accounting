package com.ogl4jo3.accounting.ui.accountMgmt

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.accountmanagement.Account
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.string.StringUtil

class AccountNewEditFragment : Fragment() {

    private val args: AccountNewEditFragmentArgs by navArgs()

    //UI元件
    private var etAccountName: EditText? = null
    private var etStartingAmount: EditText? = null
    private var ivAccountCategoryIcon: ImageView? = null
    private var spAccountCategory: Spinner? = null
    private var swDefaultAccount: Switch? = null
    private var btnNew //新增按鈕
            : Button? = null

    //編輯時用
    private var account: Account? = null
    private var btnSave //儲存按鈕
            : Button? = null
    private var btnDel //刪除按鈕
            : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = Gson().fromJson(args.accountJsonStr, Account::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_new_edit, container, false)
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
        etAccountName = view.findViewById<View>(R.id.et_account_name) as EditText
        etStartingAmount = view.findViewById<View>(R.id.et_starting_amount) as EditText
        ivAccountCategoryIcon = view.findViewById<View>(R.id.iv_account_category_icon) as ImageView
        spAccountCategory = view.findViewById<View>(R.id.sp_account_category) as Spinner
        swDefaultAccount = view.findViewById<View>(R.id.sw_default_account) as Switch
        btnNew = view.findViewById<View>(R.id.btn_new) as Button
        //編輯時用
        btnSave = view.findViewById<View>(R.id.btn_save) as Button
        btnDel = view.findViewById<View>(R.id.btn_del) as Button
    }

    /**
     * 設置元件資料
     */
    private fun setViewData() {
        if (args.accountJsonStr.isNotEmpty()) { //    編輯時
            btnNew!!.visibility = View.GONE
            btnSave!!.visibility = View.VISIBLE
            val db = MyDBHelper.getDatabase(activity)
            if (AccountDAO(db).numberOfAccounts > 1) { //若帳戶數量大於一個才顯示刪除按鈕
                btnDel!!.visibility = View.VISIBLE
            }
            etAccountName!!.setText(account!!.name)
            etStartingAmount!!.setText(account!!.startingAmount.toString())
            ivAccountCategoryIcon!!.setImageResource(account!!.categoryIcon)
            spAccountCategory!!.setSelection(account!!.category)
            swDefaultAccount!!.isChecked = account!!.isDefaultAccount
        }
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        spAccountCategory!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View, position: Int,
                l: Long
            ) {
                var categoryIcon = 0
                when (position) {
                    Account.CATEGORY_CASH -> categoryIcon = R.drawable.ic_account_category_cash
                    Account.CATEGORY_CARD -> categoryIcon = R.drawable.ic_account_category_card
                    Account.CATEGORY_BANK -> categoryIcon = R.drawable.ic_account_category_bank
                }
                ivAccountCategoryIcon!!.setImageResource(categoryIcon)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        btnNew!!.setOnClickListener(View.OnClickListener {
            val db = MyDBHelper.getDatabase(activity)
            val accountName = etAccountName!!.text.toString()
            if (StringUtil.isNullorEmpty(accountName)) {
                Toast.makeText(
                    activity, R.string.msg_input_account_name,
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else if (AccountDAO(db).accountNameCount(accountName) > 0) {
                Toast.makeText(
                    activity, R.string.msg_account_name_exist,
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val account = Account()
            account.name = accountName
            account.startingAmount = Integer.valueOf(etStartingAmount!!.text.toString())
            account.category = spAccountCategory!!.selectedItemPosition
            account.isDefaultAccount = swDefaultAccount!!.isChecked
            AccountDAO(db).newAccount(account)
            findNavController().popBackStack()
        })
        //編輯用
        btnSave!!.setOnClickListener(View.OnClickListener {
            val db = MyDBHelper.getDatabase(activity)
            val accountName = etAccountName!!.text.toString()
            if (StringUtil.isNullorEmpty(accountName)) {
                Toast.makeText(
                    activity, R.string.msg_input_account_name,
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else if (AccountDAO(db).accountNameCount(accountName) > 1) {
                Toast.makeText(
                    activity, R.string.msg_account_name_exist,
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val accountNew = account
            accountNew!!.name = accountName
            accountNew.startingAmount = Integer.valueOf(etStartingAmount!!.text.toString())
            accountNew.category = spAccountCategory!!.selectedItemPosition
            accountNew.isDefaultAccount = swDefaultAccount!!.isChecked
            AccountDAO(db).saveData(accountNew)
            findNavController().popBackStack()
        })
        btnDel!!.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.msg_expense_del_confirm)
            builder.setMessage(R.string.msg_account_del_confirm_hint)
            builder.setPositiveButton(R.string.btn_del) { _, _ ->
                val db = MyDBHelper.getDatabase(activity)
                AccountDAO(db).delAccount(account)
                Toast.makeText(
                    activity,
                    getString(R.string.msg_account_deleted, account!!.name),
                    Toast.LENGTH_SHORT
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