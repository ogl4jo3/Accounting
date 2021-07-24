package com.ogl4jo3.accounting.ui.expense

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentExpenseEditBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*


class ExpenseEditFragment : Fragment() {

    private val binding by viewBinding(FragmentExpenseEditBinding::inflate)
    private val args: ExpenseEditFragmentArgs by navArgs()
    private val viewModel by viewModel<ExpenseEditViewModel> {
        parametersOf(args.expenseRecord)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseEditFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDel.setOnClickListener { showDelConfirmDialog() }
        }
        viewModel.apply {
            moneyInputError = {
                binding.tilMoney.error = getString(R.string.msg_input_money)
            }
            navToExpenseFragment = {
                findNavController().navigate(
                    ExpenseEditFragmentDirections.actionExpenseEditFragmentToExpenseFragment(
                        args.expenseRecord.recordTime
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_save) {
            binding.tilMoney.error = null
            viewModel.saveExpenseRecord()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDelConfirmDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(getString(R.string.msg_expense_del_confirm))
            setPositiveButton(R.string.btn_del) { _, _ ->
                viewModel.deleteExpenseRecord()
            }
            setNegativeButton(R.string.btn_cancel) { dialog, _ -> dialog.dismiss() }
            create().show()
        }
    }

}