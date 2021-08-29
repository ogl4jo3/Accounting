package com.ogl4jo3.accounting.ui.accountingnotification

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.AccountingNotification
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
            rvNotification.adapter =
                AccountingNotificationAdapter(this@AccountingNotificationFragment.viewModel)
        }
        viewModel.apply {
            notificationMaximumError = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.hint_notification_maximum),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            notificationExistError = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.hint_duplicated_notification),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            showDelConfirmDialog = this@AccountingNotificationFragment::showDelConfirmDialog
            showTimePickerDialog = this@AccountingNotificationFragment::showTimePickerDialog
            updateNotificationList()
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
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(21)
                    .setMinute(30)
                    .build()

                picker.addOnPositiveButtonClickListener {
                    viewModel.addNotification(picker.hour, picker.minute, true)
                }
                picker.show(parentFragmentManager, "tag")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDelConfirmDialog(notification: AccountingNotification) {
        AlertDialog.Builder(activity).apply {
            setTitle(getString(R.string.msg_notification_del_confirm))
            setPositiveButton(R.string.btn_del) { _, _ ->
                viewModel.deleteNotification(
                    notification,
                    onSuccess = { },
                    onFail = {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.msg_at_least_one_notification),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            setNegativeButton(R.string.btn_cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            create().show()
        }
    }

    private fun showTimePickerDialog(
        notification: AccountingNotification,
        onSuccess: (notification: AccountingNotification) -> Unit = {}
    ) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(notification.hour)
            .setMinute(notification.minute)
            .build()

        picker.addOnPositiveButtonClickListener {
            viewModel.updateNotification(
                notification.id,
                picker.hour,
                picker.minute,
                notification.isOn,
                onSuccess
            )
        }
        picker.show(parentFragmentManager, "tag")
    }

}