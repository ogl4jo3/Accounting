package com.ogl4jo3.accounting.ui.accountingnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.databinding.FragmentAccountingNotificationBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountingNotificationFragment : BaseFragment() {

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

        }
        viewModel.apply {
            updateFailed = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.msg_update_notification_failed),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            showTimePickerDialog = this@AccountingNotificationFragment::showTimePickerDialog
        }
    }

    private fun showTimePickerDialog(notification: AccountingNotification) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(notification.hour)
            .setMinute(notification.minute)
            .build()

        picker.addOnPositiveButtonClickListener {
            viewModel.updateNotificationTime(picker.hour, picker.minute, notification)
        }
        picker.show(parentFragmentManager, "tag")
    }

}