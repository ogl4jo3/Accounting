package com.ogl4jo3.accounting.ui.income

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.income.Income
import com.ogl4jo3.accounting.common.income.IncomeDAO
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.date.DateUtil
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesHelper
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesTag
import com.ogl4jo3.accounting.utils.string.StringUtil
import java.text.ParseException
import java.util.*

class IncomeFragment : Fragment() {
    // UI 元件
    private var tvDate //日期
            : TextView? = null
    private var tvThisDayIncome //本日收入
            : TextView? = null
    private var tvThisMonthIncome //本月收入
            : TextView? = null
    private var tvIncomePercent //收入百分比
            : TextView? = null
    private var tvNoData //沒資料時顯示
            : TextView? = null
    private var rvIncomeItem: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: IncomeAdapter? = null
    private var dateStr: String? = null
    private var incomeList: MutableList<Income>? = null
    private var thisDayIncome //本日收入金額
            = 0
    private var thisMonthIncome //本月收入金額
            = 0
    private var incomePercent //收入百分比
            = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        dateStr = DateUtil.getCurrentDate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_income, container, false)
        initUI(view)
        initIncomeList() //需放在onCreateView，從新增或編輯頁跳回時才會重載資料
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

                //依據日期更新 incomeList資料，並刷新Adapter
                updateIncomeList(dateStr)
            }, mCalendar[Calendar.YEAR], mCalendar[Calendar.MONTH],
                    mCalendar[Calendar.DAY_OF_MONTH]).show()
        } else if (id == R.id.menu_new) {
            findNavController().navigate(IncomeFragmentDirections
                    .actionIncomeFragmentToIncomeNewEditFragment(
                            title = resources.getString(R.string.title_income_new),
                            dateStr = tvDate!!.text.toString(),
                            incomeId = null
                    ))
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 依據日期
     * 初始化資料
     */
    private fun initIncomeList() {
        val db = MyDBHelper.getDatabase(activity)
        incomeList = IncomeDAO(db).getByDate(dateStr) as MutableList<Income>
        if (incomeList!!.size <= 0) {
            rvIncomeItem!!.visibility = View.GONE
            tvNoData!!.visibility = View.VISIBLE
        } else {
            rvIncomeItem!!.visibility = View.VISIBLE
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
        rvIncomeItem = view.findViewById<View>(R.id.rv_income_item) as RecyclerView
        tvDate = view.findViewById<View>(R.id.tv_date) as TextView
        tvThisDayIncome = view.findViewById<View>(R.id.tv_this_day_income) as TextView
        tvThisMonthIncome = view.findViewById<View>(R.id.tv_this_month_income) as TextView
        tvIncomePercent = view.findViewById<View>(R.id.tv_income_percent) as TextView
    }

    /**
     * 設置元件資料
     */
    private fun setViewData() {
        tvDate!!.text = dateStr
        thisDayIncome = calculateThisDayIncome()
        thisMonthIncome = calculateThisMonthIncome()
        incomePercent = if (thisMonthIncome == 0) {
            0
        } else {
            thisDayIncome * 100 / thisMonthIncome
        }
        tvThisDayIncome!!.text = StringUtil.toMoneyStr(thisDayIncome)
        tvThisMonthIncome!!.text = StringUtil.toMoneyStr(thisMonthIncome)
        tvIncomePercent!!.text = incomePercent.toString()
    }

    /**
     * 設置RecyclerView
     */
    private fun setRecyclerView() {
        rvIncomeItem!!.setHasFixedSize(true)
        // Layout管理器
        mLayoutManager = LinearLayoutManager(this.activity)
        rvIncomeItem!!.layoutManager = mLayoutManager
        // Adapter
        mAdapter = IncomeAdapter(requireActivity(), requireFragmentManager(), incomeList!!)
        rvIncomeItem!!.adapter = mAdapter
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        tvDate!!.setOnClickListener {
            dateStr = DateUtil.getCurrentDate()
            //new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
            tvDate!!.text = dateStr
            //依據日期更新 incomeList資料，並刷新Adapter
            updateIncomeList(dateStr)
        }
    }

    /**
     * 依據日期更新 incomeList資料，並刷新Adapter
     */
    private fun updateIncomeList(dateStr: String?) {
        val db = MyDBHelper.getDatabase(activity)
        incomeList!!.clear()
        incomeList!!.addAll(IncomeDAO(db).getByDate(dateStr))
        mAdapter!!.notifyDataSetChanged()
        thisDayIncome = calculateThisDayIncome()
        thisMonthIncome = calculateThisMonthIncome()
        incomePercent = if (thisMonthIncome == 0) {
            0
        } else {
            thisDayIncome * 100 / thisMonthIncome
        }
        //更新本日收入欄位
        tvThisDayIncome!!.text = StringUtil.toMoneyStr(thisDayIncome)
        //更新本月收入欄位
        tvThisMonthIncome!!.text = StringUtil.toMoneyStr(thisMonthIncome)
        //更新收入百分比欄位
        tvIncomePercent!!.text = incomePercent.toString()
        if (incomeList!!.size <= 0) {
            rvIncomeItem!!.visibility = View.GONE
            tvNoData!!.visibility = View.VISIBLE
        } else {
            rvIncomeItem!!.visibility = View.VISIBLE
            tvNoData!!.visibility = View.GONE
        }
    }

    /**
     * 計算本日收入
     */
    private fun calculateThisDayIncome(): Int {
        var thisDayIncome = 0
        for (income in incomeList!!) {
            thisDayIncome += income.price
        }
        return thisDayIncome
    }

    /**
     * 計算本月總收入
     */
    private fun calculateThisMonthIncome(): Int {

        //取得預算起始日
        val budgetStartDay = SharedPreferencesHelper(activity, SharedPreferencesTag.prefsData)
                .getString(SharedPreferencesTag.prefsBudgetStartDay, "01")
        var date: Date? = Date()
        try {
            date = DateUtil.strToDate(tvDate!!.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val db = MyDBHelper.getDatabase(activity)
        return IncomeDAO(db)
                .getSumByDateAccount(DateUtil.fromDateStrByMonth(date, budgetStartDay),
                        DateUtil.toDateStrByMonth(date, budgetStartDay), AccountDAO.ALL_ACCOUNT)
    }

}