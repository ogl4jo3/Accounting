package com.ogl4jo3.accounting.ui.instruction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ogl4jo3.accounting.R

class InstructionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = InstructionAdapter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    companion object {
        const val INSTRUCTION_PAGE_COUNT = 4
        const val ARG_INSTRUCTION_PAGE_NUM = "ARG_INSTRUCTION_PAGE_NUM"
    }
}