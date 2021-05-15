package com.ogl4jo3.accounting.ui.categoryMgmt

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.categorymanagement.Category
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import timber.log.Timber
import java.util.*

/**
 * 支出類別itemAdapter
 * Created by ogl4jo3 on 2017/7/14.
 */
class ExpensesCategoryAdapter(
    private val mContext: Context, fragmentManager: FragmentManager,
    private val categoryList: List<Category>
) : CategoryAdapter(mContext, fragmentManager, categoryList) {
    override fun onItemClick(holder: ViewHolder) {
        Navigation.findNavController(holder.itemView).navigate(
            ExpensesCategoryMgmtFragmentDirections.actionExpenseCategoryMgmtFragmentToExpensesCategoryNewEditFragment(
                title = mContext.getString(R.string.title_expense_category_edit),
                categoryJsonStr = Gson().toJson(categoryList[holder.adapterPosition])
            )
        )
        //Toast.makeText(mContext, "Click on " + categoryList.get(position).getName(),
        //		Toast.LENGTH_SHORT).show();
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        //更新排列順序
        val db = MyDBHelper.getDatabase(mContext)
        CategoryDAO(db).updateExpensesOrderNum(
            categoryList[fromPosition],
            categoryList[toPosition]
        )

        //即時更新資料
        val fromOrderNum = categoryList[fromPosition].orderNum
        val toOrderNum = categoryList[toPosition].orderNum
        categoryList[fromPosition].orderNum = toOrderNum
        categoryList[toPosition].orderNum = fromOrderNum
        Timber.d("onItemMove: from:$fromPosition, to:$toPosition")
        Collections.swap(categoryList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {

        //刪除
        val db = MyDBHelper.getDatabase(mContext)
        CategoryDAO(db).deleteExpensesData(categoryList[position])
        Toast.makeText(
            mContext, mContext.getString(
                R.string.msg_category_deleted, categoryList[position]
                    .name
            ),
            Toast.LENGTH_SHORT
        ).show()
        categoryList.toMutableList().removeAt(position)//TODO: workaround, check remove method
        notifyItemRemoved(position)
    }
}