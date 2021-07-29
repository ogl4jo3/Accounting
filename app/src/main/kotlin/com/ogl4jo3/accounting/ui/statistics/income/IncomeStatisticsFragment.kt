package com.ogl4jo3.accounting.ui.statistics.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentIncomeStatisticsBinding
import com.ogl4jo3.accounting.ui.common.viewBinding
import com.ogl4jo3.accounting.ui.statistics.StatisticsItemAdapter
import com.ogl4jo3.accounting.ui.statistics.TabStatisticsUnit
import org.koin.androidx.viewmodel.ext.android.viewModel

class IncomeStatisticsFragment : Fragment() {

    private val binding by viewBinding(FragmentIncomeStatisticsBinding::inflate)
    private val viewModel by viewModel<IncomeStatisticsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@IncomeStatisticsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvStatisticsItems.adapter =
                StatisticsItemAdapter(this@IncomeStatisticsFragment.viewModel)
            addTabItems(tabLayout)
        }
        viewModel.apply {

        }
    }

    private fun addTabItems(tabLayout: TabLayout) {
        TabStatisticsUnit.values().forEach {
            when (it) {
                TabStatisticsUnit.MONTH -> {
                    tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_month))
                }
                TabStatisticsUnit.YEAR -> {
                    tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_year))
                }
            }
        }
    }

}