package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountAddBinding
import com.ogl4jo3.accounting.ui.common.viewBinding
import com.ogl4jo3.accounting.utils.keyboard.KeyboardUtil

class AccountAddFragment : Fragment() {

    private val binding by viewBinding(FragmentAccountAddBinding::inflate)
    private val viewModel by viewModels<AccountAddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@AccountAddFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            navPopBackStack = { findNavController().popBackStack() }
            accountNameEmptyError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_input_account_name)
            }
            accountNameExistError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_account_name_exist)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtil.closeKeyboard(activity)
    }
}