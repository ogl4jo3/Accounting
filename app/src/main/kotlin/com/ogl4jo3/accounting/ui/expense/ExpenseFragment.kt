package com.ogl4jo3.accounting.ui.expense

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.expenses.Expenses
import com.ogl4jo3.accounting.common.expenses.ExpensesDAO
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.date.DateUtil
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesHelper
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesTag
import com.ogl4jo3.accounting.utils.string.StringUtil
import java.text.ParseException
import java.util.*

class ExpenseFragment : Fragment() {
    // UI 元件
    private var tvDate //日期
            : TextView? = null
    private var tvThisDayExpenses //本日支出
            : TextView? = null
    private var tvRemainingBudget //剩餘預算
            : TextView? = null
    private var tvTotalBudget //預算總額
            : TextView? = null
    private var tvBudgetPercent //預算百分比
            : TextView? = null
    private var tvNoData //沒資料時顯示
            : TextView? = null
    private var rvExpensesItem: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: ExpenseAdapter? = null
    private var dateStr: String? = null
    private var expensesList: MutableList<Expenses>? = null
    private var totalRemainingBudget //剩餘預算總額，所有帳戶的
            = 0
    private var totalBudget //所有帳戶預算相加
            = 0
    private var budgetPercent //當剩餘預算總額變動時，會跟著變動
            = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        dateStr = DateUtil.getCurrentDate()
        val database = MyDBHelper.getDatabase(activity)
        totalBudget = AccountDAO(database).budgetSumOfAccounts

        //new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expense, container, false)
        initUI(view)
        initExpensesList() //需放在onCreateView，從新增或編輯頁跳回時才會重載資料
        setViewData()
        setRecyclerView()
        setOnClickListener()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_expenses_income, menu)
        //super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_date) {
            var date: Date? = null
            try {
                date = DateUtil.strToDate(tvDate!!.text.toString())
                //new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).parse(tvDate.getText().toString());
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val mCalendar = Calendar.getInstance()
            mCalendar.time = date
            DatePickerDialog(requireActivity(), { view, year, month, day ->
                mCalendar[Calendar.YEAR] = year
                mCalendar[Calendar.MONTH] = month
                mCalendar[Calendar.DAY_OF_MONTH] = day
                dateStr = DateUtil.dateToStr(mCalendar.time)
                //new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(mCalendar.getTime());
                tvDate!!.text = dateStr

                //依據日期更新 expensesList資料，並刷新Adapter
                updateExpensesList(dateStr)
            }, mCalendar[Calendar.YEAR], mCalendar[Calendar.MONTH],
                    mCalendar[Calendar.DAY_OF_MONTH]).show()
        } else if (id == R.id.menu_new) {
            findNavController().navigate(ExpenseFragmentDirections
                    .actionExpenseFragmentToExpenseNemEditFragment(
                            title = resources.getString(R.string.title_expenses_new),
                            dateStr = tvDate!!.text.toString(),
                            expenseId = null
                    ))
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 依據日期
     * 初始化資料
     */
    private fun initExpensesList() {
        //新增測試用資料
        //SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
        //new ExpensesDAO(db).newTestExpensesData();
        val db = MyDBHelper.getDatabase(activity)
        expensesList = ExpensesDAO(db).getByDate(dateStr) as MutableList<Expenses>
        /*for (int i = 0; i < expensesList.size(); i++) {
			Log.d(TAG, "after listExpenses(" + i + "): " + expensesList.get(i).toString());
		}*/if (expensesList!!.size <= 0) { //若沒資料時，顯示無資料
            rvExpensesItem!!.visibility = View.GONE
            tvNoData!!.visibility = View.VISIBLE
        } else {
            rvExpensesItem!!.visibility = View.VISIBLE
            tvNoData!!.visibility = View.GONE
        }
    }

    /**
     * 初始化元件
     *
     * @param view View
     */
    private fun initUI(view: View) {
        tvNoData = view.findViewById<View>(R.id.tv_no_data) as TextView
        rvExpensesItem = view.findViewById<View>(R.id.rv_expenses_item) as RecyclerView
        tvDate = view.findViewById<View>(R.id.tv_date) as TextView
        tvThisDayExpenses = view.findViewById<View>(R.id.tv_this_day_expenses) as TextView
        tvRemainingBudget = view.findViewById<View>(R.id.tv_remaining_budget) as TextView
        tvTotalBudget = view.findViewById<View>(R.id.tv_total_budget) as TextView
        tvBudgetPercent = view.findViewById<View>(R.id.tv_budget_percent) as TextView
    }

    /**
     * 設置元件資料
     */
    private fun setViewData() {
        tvDate!!.text = dateStr
        tvThisDayExpenses!!.text = StringUtil.toMoneyStr(calculateThisDayExpenses())
        totalRemainingBudget = calculateRemainingBudget() //計算所有帳戶預算餘額
        tvRemainingBudget!!.text = StringUtil.toMoneyStr(totalRemainingBudget)
        tvTotalBudget!!.text = StringUtil.toMoneyStr(totalBudget)
        tvBudgetPercent!!.text = budgetPercent.toString()
    }

    /**
     * 設置RecyclerView
     */
    private fun setRecyclerView() {
        rvExpensesItem!!.setHasFixedSize(true)
        // Layout管理器
        mLayoutManager = LinearLayoutManager(this.activity)
        rvExpensesItem!!.layoutManager = mLayoutManager
        // Adapter
        mAdapter = ExpenseAdapter(requireContext(), requireFragmentManager(), expensesList!!)
        rvExpensesItem!!.adapter = mAdapter
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        tvDate!!.setOnClickListener {
            dateStr = DateUtil.getCurrentDate()
            //new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
            tvDate!!.text = dateStr
            //依據日期更新 expensesList資料，並刷新Adapter
            updateExpensesList(dateStr)
        }
    }

    /**
     * 依據日期更新 expensesList資料，並刷新Adapter
     */
    private fun updateExpensesList(dateStr: String?) {
        val db = MyDBHelper.getDatabase(activity)
        expensesList!!.clear()
        expensesList!!.addAll(ExpensesDAO(db).getByDate(dateStr))
        mAdapter!!.notifyDataSetChanged()
        //更新本日支出欄位
        tvThisDayExpenses!!.text = StringUtil.toMoneyStr(calculateThisDayExpenses())
        totalRemainingBudget = calculateRemainingBudget() //計算所有帳戶預算餘額
        //更新預算餘額、百分比
        tvRemainingBudget!!.text = StringUtil.toMoneyStr(totalRemainingBudget)
        tvBudgetPercent!!.text = budgetPercent.toString()
        if (expensesList!!.size <= 0) { //若沒資料時，顯示無資料
            rvExpensesItem!!.visibility = View.GONE
            tvNoData!!.visibility = View.VISIBLE
        } else {
            rvExpensesItem!!.visibility = View.VISIBLE
            tvNoData!!.visibility = View.GONE
        }
    }

    /**
     * 計算本日支出
     */
    private fun calculateThisDayExpenses(): Int {
        var thisDayExpenses = 0
        for (expenses in expensesList!!) {
            thisDayExpenses += expenses.price
        }
        return thisDayExpenses
    }

    /**
     * 所有帳戶剩餘預算相加
     * 計算這個月剩餘預算
     * 剩餘預算=總預算-支出
     */
    private fun calculateRemainingBudget(): Int {
        var totalRemainingBudget = 0
        //取得預算起始日
        val budgetStartDay = SharedPreferencesHelper(activity, SharedPreferencesTag.prefsData)
                .getString(SharedPreferencesTag.prefsBudgetStartDay, "01")
        var date: Date? = Date()
        try {
            date = DateUtil.strToDate(tvDate!!.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val database = MyDBHelper.getDatabase(activity)
        val accountList = AccountDAO(database).all
        for (account in accountList) {
            var remainingBudget: Int
            val totalBudget = account.budgetPrice
            val expensesPrice = ExpensesDAO(database)
                    .getSumByDateAccount(DateUtil.fromDateStrByMonth(date, budgetStartDay),
                            DateUtil.toDateStrByMonth(date, budgetStartDay), account.name)
            remainingBudget = totalBudget - expensesPrice
            totalRemainingBudget += remainingBudget
        }
        //剩餘預算百分比
        budgetPercent = if (totalBudget == 0 || totalRemainingBudget <= 0) { //若預算總額為0或總預算餘額小於等於零時
            0
        } else {
            totalRemainingBudget * 100 / totalBudget
        }
        return totalRemainingBudget
    }

    companion object {
        const val EXPENSES_FRAGMENT_TAG = "EXPENSES_FRAGMENT_TAG"

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpensesFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): ExpenseFragment {
            val fragment = ExpenseFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}