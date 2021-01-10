package com.ogl4jo3.accounting.ui.income

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.income.Income
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.string.StringUtil

/**
 * 收入item Adapter
 * Created by ogl4jo3 on 2017/7/27.
 */
class IncomeAdapter(private val mContext: Context, private val fragmentManager: FragmentManager,
                    private val incomeList: List<Income>) : RecyclerView.Adapter<IncomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return null;
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_expenses_income, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryID = incomeList[position].categoryId
        val db = MyDBHelper.getDatabase(mContext)
        val categoryTmp = CategoryDAO(db).getIncomeData(categoryID)
        holder.tvCategoryName.text = categoryTmp.name
        holder.ivCategoryIcon.setImageResource(categoryTmp.icon)
        holder.tvMoney.text = StringUtil.toMoneyStr(incomeList[position].price)
        holder.itemView.setOnClickListener { //Toast.makeText(mContext, "item click\n" + incomeList.get(position).toString(),
            //		Toast.LENGTH_SHORT).show();
            val income = incomeList[holder.adapterPosition]
            Navigation.findNavController(holder.itemView).navigate(IncomeFragmentDirections
                    .actionIncomeFragmentToIncomeNewEditFragment(
                            title = mContext.getString(R.string.title_income_edit),
                            dateStr = income.recordTime,
                            incomeId = income.id.toString()
                    ))
        }
    }

    override fun getItemCount(): Int {
        return incomeList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCategoryIcon: ImageView
        var tvCategoryName: TextView
        var tvMoney: TextView

        init {
            ivCategoryIcon = itemView.findViewById<View>(R.id.iv_category_icon) as ImageView
            tvCategoryName = itemView.findViewById<View>(R.id.tv_category_name) as TextView
            tvMoney = itemView.findViewById<View>(R.id.tv_money) as TextView
        }
    }
}