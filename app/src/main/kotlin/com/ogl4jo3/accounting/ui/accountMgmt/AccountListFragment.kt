package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountListBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountListFragment : BaseFragment() {

    private val binding by viewBinding(FragmentAccountListBinding::inflate)
    private val viewModel by viewModel<AccountListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AccountListFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvAccount.adapter = AccountAdapter(this@AccountListFragment.viewModel)
        }
        viewModel.apply {
            navigateToAccountEditFragment = { account ->
                findNavController().navigate(
                    AccountListFragmentDirections.actionAccountListFragmentToAccountEditFragment(
                        account = account
                    )
                )
            }
            updateAccountList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_add) {
            findNavController().navigate(
                AccountListFragmentDirections.actionAccountListFragmentToAccountAddFragment()
            )
        }
        return super.onOptionsItemSelected(item)
    }
}