package com.ogl4jo3.accounting.ui.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentIncomeBinding
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.TimeZone


class IncomeFragment : Fragment() {

    private val binding by viewBinding(FragmentIncomeBinding::inflate)
    private val args: IncomeFragmentArgs by navArgs()
    private val viewModel by viewModel<IncomeViewModel> {
        parametersOf(args.date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@IncomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvIncomeRecords.adapter = IncomeRecordAdapter(this@IncomeFragment.viewModel)
        }
        viewModel.apply {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_date_n_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_date) {
            MaterialDatePicker.Builder.datePicker()
                .setSelection(
                    viewModel.date.value?.time?.let {
                        it + TimeZone.getDefault().getOffset(it)
                    })
                .build().apply {
                    addOnPositiveButtonClickListener {
                        viewModel.pickDate(it)
                    }
                }.show(parentFragmentManager, "")
        } else if (id == R.id.menu_add) {
            viewModel.date.value?.let {
                findNavController().navigate(
                    IncomeFragmentDirections.actionIncomeFragmentToIncomeAddFragment(it)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}