package com.ogl4jo3.accounting.ui.accountingnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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
            showTimePickerDialog = this@AccountingNotificationFragment::showTimePickerDialog
            updateNotificationList()
        }
    }

    private fun showTimePickerDialog(
        notification: AccountingNotification,
        notifyItemChanged: (notification: AccountingNotification) -> Unit = {}
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
                notifyItemChanged
            )
        }
        picker.show(parentFragmentManager, "tag")
    }

}