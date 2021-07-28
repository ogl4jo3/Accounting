package com.ogl4jo3.accounting.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ogl4jo3.accounting.databinding.FragmentExpenseStatisticsBinding
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ExpenseStatisticsFragment : Fragment() {

    private val binding by viewBinding(FragmentExpenseStatisticsBinding::inflate)
    private val viewModel by viewModel<ExpenseStatisticsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseStatisticsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvStatisticsItems.adapter =
                StatisticsItemAdapter(this@ExpenseStatisticsFragment.viewModel)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Timber.e("onTabSelected")
                    Timber.e("tab?.id: ${tab?.id}")
                    Timber.e("tab?.position: ${tab?.position}")
                    //TODO: two way binding
                    this@ExpenseStatisticsFragment.viewModel.tabUnit.value =
                        if (tab?.position == 1) {
                            TabStatisticsUnit.YEAR
                        } else {
                            TabStatisticsUnit.MONTH
                        }

                    //TODO: do something, ex: update data and layout
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    Timber.e("onTabUnselected")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    Timber.e("onTabReselected")
                }
            })
        }
        viewModel.apply {

        }
    }

}