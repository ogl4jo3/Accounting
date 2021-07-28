package com.ogl4jo3.accounting.ui.statistics

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ogl4jo3.accounting.R
import java.util.Date

@BindingAdapter("tabBarDate", "tabBarUnit")
fun setTabBarDateText(textView: TextView, date: Date, tabUnit: TabStatisticsUnit) {
    textView.apply {
        text = when (tabUnit) {
            TabStatisticsUnit.YEAR -> date.yearText(context)
            TabStatisticsUnit.MONTH -> date.yearNMonthText(context)
        }
    }
}

@BindingAdapter("statisticsItems")
fun bindStatisticsItems(recyclerView: RecyclerView, statisticsItems: List<StatisticsItem>) {
    (recyclerView.adapter as StatisticsItemAdapter).submitList(statisticsItems)
}

@BindingAdapter("price")
fun setPriceText(textView: TextView, price: Int) {
    textView.apply {
        text = context.getString(R.string.tv_price, price)
    }
}

@BindingAdapter("percent")
fun setPercentText(textView: TextView, percent: Float) {
    textView.apply {
        text = String.format(context.getString(R.string.tv_percent), percent * 100)
    }
}

@BindingAdapter("statisticsUnit")
fun setStatisticsUnit(tabLayout: TabLayout, statisticsUnit: TabStatisticsUnit) {
    if (tabLayout.selectedTabPosition != statisticsUnit.ordinal) {
        tabLayout.selectTab(tabLayout.getTabAt(statisticsUnit.ordinal))
    }
}

@InverseBindingAdapter(attribute = "statisticsUnit")
fun getStatisticsUnit(tabLayout: TabLayout): TabStatisticsUnit? {
    return try {
        TabStatisticsUnit.values()[tabLayout.selectedTabPosition]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

@BindingAdapter("statisticsUnitAttrChanged")
fun setListeners(tabLayout: TabLayout, attrChange: InverseBindingListener) {
    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            attrChange.onChange()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    })
}