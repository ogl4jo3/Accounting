package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountAddBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountAddFragment : BaseFragment() {

    private val binding by viewBinding(FragmentAccountAddBinding::inflate)
    private val viewModel by viewModel<AccountAddViewModel>()

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
            navToAccountListFragment = {
                findNavController().navigate(
                    AccountAddFragmentDirections.actionAccountAddFragmentToAccountListFragment()
                )
            }
            accountNameEmptyError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_type_account_name)
            }
            accountNameExistError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_account_name_exist)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }
}