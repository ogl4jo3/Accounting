package com.ogl4jo3.accounting.ui.instruction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ogl4jo3.accounting.MainActivity
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentInstructionBinding
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity.Companion.ARG_INSTRUCTION_PAGE_NUM
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity.Companion.INSTRUCTION_PAGE_COUNT
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesHelper
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesTag

class InstructionFragment : Fragment() {

    private lateinit var binding: FragmentInstructionBinding

    private var instructionPagePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_INSTRUCTION_PAGE_NUM) }?.apply {
            instructionPagePosition = getInt(ARG_INSTRUCTION_PAGE_NUM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentInstructionBinding.inflate(inflater, container, false).apply {
            clInstruction.setBackgroundResource(
                    when (instructionPagePosition) {
                        0 -> R.drawable.use_teaching_new_expenses360
                        1 -> R.drawable.use_teaching_choose_date360
                        2 -> R.drawable.use_teaching_function_menu360
                        else -> R.drawable.use_teaching_custom_date360
                    }
            )
            btnStartUse.visibility = if (instructionPagePosition == INSTRUCTION_PAGE_COUNT - 1) VISIBLE else GONE
            btnStartUse.setOnClickListener { startUse() }
        }

        return binding.root
    }

    private fun startUse() {
        //將第一次使用改為否
        SharedPreferencesHelper(activity, SharedPreferencesTag.prefsData)
                .setBoolean(SharedPreferencesTag.prefsFirstUse, false)
        //開始使用
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}