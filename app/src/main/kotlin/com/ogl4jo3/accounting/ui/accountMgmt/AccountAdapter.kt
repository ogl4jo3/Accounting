package com.ogl4jo3.accounting.ui.accountMgmt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.accountmanagement.Account
import com.ogl4jo3.accounting.utils.string.StringUtil

/**
 * 帳戶Adapter
 * Created by ogl4jo3 on 2017/8/10.
 */
class AccountAdapter(
    private val mContext: Context, private val fragmentManager: FragmentManager,
    private val accountList: List<Account>
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return null;
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivCategoryIcon.setImageResource(accountList[position].categoryIcon)
        holder.tvCategoryName.text = accountList.get(position).categoryName
        holder.tvAccountName.text = accountList.get(position).name
        if (accountList[position].isDefaultAccount) {
            holder.tvDefault.visibility = View.VISIBLE
        }
        holder.tvBalance.text = StringUtil.toMoneyStr(
            accountList.get(position).balance
        )
        holder.itemView.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(holder.itemView).navigate(
                AccountMgmtFragmentDirections.actionAccountMgmtFragmentToAccountNewEditFragment(
                    title = mContext.getString(R.string.title_account_edit),
                    accountJsonStr = Gson().toJson(accountList[holder.adapterPosition])
                )
            )
        })
    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCategoryIcon: ImageView
        var tvAccountName: TextView
        var tvDefault: TextView
        var tvCategoryName: TextView
        var tvBalance: TextView

        init {
            ivCategoryIcon = itemView.findViewById<View>(R.id.iv_account_category_icon) as ImageView
            tvAccountName = itemView.findViewById<View>(R.id.tv_account_name) as TextView
            tvDefault = itemView.findViewById<View>(R.id.tv_default_account) as TextView
            tvCategoryName = itemView.findViewById<View>(R.id.tv_account_category_name) as TextView
            tvBalance = itemView.findViewById<View>(R.id.tv_account_balance) as TextView
        }
    }
}