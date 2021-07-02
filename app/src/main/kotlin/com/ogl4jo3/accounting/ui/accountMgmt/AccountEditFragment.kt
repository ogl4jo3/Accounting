package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountEditBinding

//TODO:未完成, use this instead of AccountNewEditFragment
class AccountEditFragment : Fragment() {

    private lateinit var binding: FragmentAccountEditBinding
    private val viewModel by viewModels<AccountEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
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

        binding = FragmentAccountEditBinding.inflate(inflater, container, false).apply {
            viewModel = this@AccountEditFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}