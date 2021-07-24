package com.ogl4jo3.accounting.ui.accountMgmt

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountEditBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AccountEditFragment : Fragment() {

    private val binding by viewBinding(FragmentAccountEditBinding::inflate)
    private val args by navArgs<AccountEditFragmentArgs>()
    private val viewModel by viewModel<AccountEditViewModel> {
        parametersOf(args.account)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AccountEditFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDel.setOnClickListener { showDelConfirmDialog() }
        }
        viewModel.apply {
            navToAccountListFragment = {
                findNavController().navigate(
                    AccountEditFragmentDirections.actionAccountEditFragmentToAccountListFragment()
                )
            }
            accountNameEmptyError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_input_account_name)
            }
            accountNameExistError = {
                binding.tilAccountName.error = null
                binding.tilAccountName.error = getString(R.string.msg_account_name_exist)
            }
            atLeastOneDefaultAccount = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.msg_at_least_one_default_account),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }

    private fun showDelConfirmDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(getString(R.string.msg_account_del_confirm, viewModel.account.name))
            setMessage(R.string.msg_account_del_confirm_hint)
            setPositiveButton(R.string.btn_del) { _, _ ->
                viewModel.deleteAccount(
                    onSuccess = {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.msg_account_deleted, viewModel.account.name),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.navToAccountListFragment()
                    },
                    onFail = {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.msg_at_least_one_account),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            setNegativeButton(R.string.btn_cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            create().show()
        }
    }
}