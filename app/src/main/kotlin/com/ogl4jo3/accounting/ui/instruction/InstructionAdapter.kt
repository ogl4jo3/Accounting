package com.ogl4jo3.accounting.ui.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity.Companion.ARG_INSTRUCTION_PAGE_NUM
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity.Companion.INSTRUCTION_PAGE_COUNT

class InstructionAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = INSTRUCTION_PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = InstructionFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_INSTRUCTION_PAGE_NUM, position)
        }
        return fragment
    }
}