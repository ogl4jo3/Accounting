package com.ogl4jo3.accounting.ui.income

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentIncomeAddBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.extensions.focusAndShowKeyboard
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.extensions.showKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*


class IncomeAddFragment : BaseFragment() {

    private val binding by viewBinding(FragmentIncomeAddBinding::inflate)
    private val args: IncomeAddFragmentArgs by navArgs()
    private val viewModel by viewModel<IncomeAddViewModel> {
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
            viewModel = this@IncomeAddFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etMoney.focusAndShowKeyboard()
        }
        viewModel.apply {
            moneyInputError = {
                binding.tilMoney.error = getString(R.string.msg_type_amount)
            }
            navToIncomeFragment = {
                findNavController().navigate(
                    IncomeAddFragmentDirections.actionIncomeAddFragmentToIncomeFragment(args.date)
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
            viewModel.addIncomeRecord()
        }
        return super.onOptionsItemSelected(item)
    }

}