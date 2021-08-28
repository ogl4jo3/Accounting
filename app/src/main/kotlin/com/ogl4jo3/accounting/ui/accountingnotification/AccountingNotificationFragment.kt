package com.ogl4jo3.accounting.ui.accountingnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentAccountingNotificationBinding
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountingNotificationFragment : Fragment() {

    private val binding by viewBinding(FragmentAccountingNotificationBinding::inflate)
    private val viewModel by viewModel<AccountingNotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@AccountingNotificationFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvNotification.adapter = AccountingNotificationAdapter()
        }
        viewModel.apply {

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                //TODO: time picker
            }
        }
        return super.onOptionsItemSelected(item)
    }
}