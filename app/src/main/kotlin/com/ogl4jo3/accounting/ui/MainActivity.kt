package com.ogl4jo3.accounting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.utils.KeyboardUtil

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KeyboardUtil.setupUI(this, findViewById(R.id.drawer_layout))
        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))

        val navController: NavController = findNavController(R.id.main_content)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.expenseFragment,
            R.id.incomeFragment,
            R.id.accountListFragment,
            R.id.expenseCategoryMgmtFragment,
            R.id.incomeCategoryMgmtFragment,
            R.id.expenseStatisticsFragment,
            R.id.incomeStatisticsFragment,
            R.id.aboutFragment
        ).setOpenableLayout(drawerLayout).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.main_content).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
    }
}