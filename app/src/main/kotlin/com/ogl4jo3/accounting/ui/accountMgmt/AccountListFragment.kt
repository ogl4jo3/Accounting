package com.ogl4jo3.accounting.ui.accountMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountListBinding

class AccountListFragment : Fragment() {

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel by viewModels<AccountListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.apply {
            navigateToAccountEditFragment = { account ->
                findNavController().navigate(
                    AccountListFragmentDirections.actionAccountListFragmentToAccountNewEditFragment(
                        title = getString(R.string.title_account_edit),
                        accountJsonStr = Gson().toJson(account)
                    )
                )
            }
        }
        binding = FragmentAccountListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AccountListFragment.viewModel
            rvAccount.layoutManager = LinearLayoutManager(context)
            rvAccount.adapter = AccountAdapter(this@AccountListFragment.viewModel)
        }
        viewModel.updateAccountList()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_add) {
            findNavController().navigate(
                AccountListFragmentDirections.actionAccountListFragmentToAccountAddFragment(
                    title = getString(R.string.title_account_add)
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }
}