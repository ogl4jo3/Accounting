package com.ogl4jo3.accounting.ui.expense

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.expenses.ExpensesDAO
import com.ogl4jo3.accounting.databinding.FragmentExpenseAddBinding
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*


class ExpenseAddFragment : Fragment() {

    private val binding by viewBinding(FragmentExpenseAddBinding::inflate)
    private val args: ExpenseAddFragmentArgs by navArgs()
    private val viewModel by viewModel<ExpenseAddViewModel> {
        parametersOf(args.date)
    }

    private val database: SQLiteDatabase by lazy { MyDBHelper.getDatabase(activity) }
    private val expensesDAO: ExpensesDAO by lazy { ExpensesDAO(database) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseAddFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
        viewModel.apply {
            moneyInputError = {
                binding.tilMoney.error = getString(R.string.msg_input_money)
            }
            accountInputError = {
                binding.spAccount.error = getString(R.string.msg_input_account)
            }
            categoryInputError = {
                binding.spCategory.error = getString(R.string.msg_input_category)
            }
            saveExpenseToDB = { expense ->
                //TODO:
                expensesDAO.newExpensesData(expense)
                findNavController().popBackStack()
            }
            updateAllAccounts()
            updateAllCategories()
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
            binding.spAccount.error = null
            binding.spCategory.error = null
            //TODO:
//            viewModel.saveExpenseRecord(
//                binding.spAccount.getName(),
//                binding.spCategory.getSelectedItem().id
//            )
        }
        return super.onOptionsItemSelected(item)
    }

}