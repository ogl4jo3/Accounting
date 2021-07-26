package com.ogl4jo3.accounting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.local.SharedPreferencesHelper
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity
import org.koin.android.ext.android.inject
import java.util.*

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val sharedPreferencesHelper: SharedPreferencesHelper by inject()

        Thread {
            val mainIntent = Intent(
                this@InitActivity,
                if (sharedPreferencesHelper.isFirstUse) InstructionActivity::class.java else MainActivity::class.java
            )
            try {
                Thread.sleep(WAIT_SECOND.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            startActivity(mainIntent)
            finish()
        }.start()
    }

    companion object {
        private const val WAIT_SECOND = 200 // 等候秒數
    }
}