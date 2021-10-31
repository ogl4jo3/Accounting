package com.ogl4jo3.accounting.ui.expense

import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentExpenseBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.viewBinding
import com.ogl4jo3.accounting.utils.cancelNotifications
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.TimeZone


class ExpenseFragment : BaseFragment() {

    private val binding by viewBinding(FragmentExpenseBinding::inflate)
    private val args: ExpenseFragmentArgs by navArgs()
    private val viewModel by viewModel<ExpenseViewModel> {
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
            viewModel = this@ExpenseFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvExpenseRecords.adapter = ExpenseRecordAdapter { item ->
                findNavController().navigate(
                    ExpenseFragmentDirections.actionExpenseFragmentToExpenseEditFragment(item)
                )
            }
        }
        viewModel.apply {

        }
        context?.applicationContext?.let { applicationContext ->
            ContextCompat.getSystemService(
                applicationContext, NotificationManager::class.java
            )?.cancelNotifications()
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
                    ExpenseFragmentDirections.actionExpenseFragmentToExpenseAddFragment(it)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}