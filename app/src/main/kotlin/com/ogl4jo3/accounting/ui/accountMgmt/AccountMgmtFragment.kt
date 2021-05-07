package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.accountmanagement.Account
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper

class AccountMgmtFragment : Fragment() {
    //UI元件
    private var btnNew //新增按鈕
            : Button? = null
    private var mRecyclerView // RecyclerView
            : RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: AccountAdapter? = null
    private lateinit var accountList: List<Account>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_mgmt, container, false)
        initAccountList()
        initUI(view)
        setRecyclerView()
        setOnClickListener()
        return view
    }

    /**
     * 初始化帳戶資料
     */
    private fun initAccountList() {
        val db = MyDBHelper.getDatabase(activity)
        accountList = AccountDAO(db).all

        /*Account account = new Account();
		account.setId(1);
		account.setName("測試用帳戶-卡片");
		account.setCategory(Account.CATEGORY_CARD);
		account.setBalance(1025);
		account.setDefaultAccount(false);
		account.setStartingAmount(50000);
		account.setBudgetPrice(10000);
		account.setBudgetNotice(20);
		Account account1 = new Account();
		account1.setId(2);
		account1.setName("測試用帳戶-銀行");
		account1.setCategory(Account.CATEGORY_BANK);
		account1.setBalance(999);
		account1.setDefaultAccount(false);
		account1.setStartingAmount(300);
		account1.setBudgetPrice(10000);
		account1.setBudgetNotice(20);
		accountList.add(account);
		accountList.add(account1);*/
    }

    /**
     * 初始化UI元件
     */
    private fun initUI(view: View) {
        btnNew = view.findViewById<View>(R.id.btn_new) as Button
        mRecyclerView = view.findViewById<View>(R.id.rv_account) as RecyclerView
    }

    /**
     * 設置RecyclerView
     */
    private fun setRecyclerView() {
        mRecyclerView!!.setHasFixedSize(true)
        // Layout管理器
        mLayoutManager = LinearLayoutManager(this.activity)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Adapter
        mAdapter = AccountAdapter(requireContext(), requireFragmentManager(), accountList)
        mRecyclerView!!.adapter = mAdapter
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        btnNew!!.setOnClickListener {
            findNavController().navigate(
                AccountMgmtFragmentDirections.actionAccountMgmtFragmentToAccountNewEditFragment(
                    title = resources.getString(R.string.title_account_new), accountJsonStr = ""
                )
            )
        }
    }

}