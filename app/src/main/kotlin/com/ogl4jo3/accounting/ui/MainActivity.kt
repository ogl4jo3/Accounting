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
import com.ogl4jo3.accounting.utils.keyboard.KeyboardUtil

class MainActivity : AppCompatActivity() {
//        , NavigationView.OnNavigationItemSelectedListener {

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
            R.id.expenseFragment, R.id.incomeFragment
        ).setOpenableLayout(drawerLayout).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)


//        //TODO: below old
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close) {
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//                KeyboardUtil.closeKeyboard(this@MainActivity)
//                super.onDrawerSlide(drawerView, slideOffset)
//            }
//        }
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()
//        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        navigationView.setNavigationItemSelectedListener(this)
//        val fragmentManager = fragmentManager
//        val expensesFragment = ExpensesFragment.newInstance("", "")
//        fragmentManager.beginTransaction().replace(R.id.layout_main_content, expensesFragment,
//                ExpensesFragment.EXPENSES_FRAGMENT_TAG).commit()
//        if (intent.extras != null && intent.extras!!.getInt(ExpensesDAO.CATEGORY_COLUMN) > 0) { //從記帳通知進入時
//            //直接帶入類別、描述
//            val categoryId = intent.extras!!.getInt(ExpensesDAO.CATEGORY_COLUMN)
//            val description = intent.extras!!.getString(ExpensesDAO.DESCRIPTION_COLUMN)
//            val expenses = Expenses()
//            expenses.id = -1
//            expenses.categoryId = categoryId
//            expenses.description = description
//            val expensesNewEditFragment = ExpensesNewEditFragment
//                    .newInstance(DateUtil.dateToStr(Date()), Gson().toJson(expenses))
//            fragmentManager.beginTransaction()
//                    .replace(R.id.layout_main_content, expensesNewEditFragment, null)
//                    .addToBackStack(null).commit()
//        }
    }

//    override fun onBackPressed() {
//        val fragmentManager = fragmentManager
//        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
//            if (fragmentManager.backStackEntryCount > 0) {
//                fragmentManager.popBackStack()
//            } else {
//                super.onBackPressed()
//            }
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        val id = item.itemId
//        val fragmentManager = fragmentManager
//        fragmentManager.popBackStackImmediate(null,
//                FragmentManager.POP_BACK_STACK_INCLUSIVE) //消除其他堆疊的Fragment
//        when (id) {
//            R.id.nav_expenses -> {
//                val expensesFragment = ExpensesFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, expensesFragment,
//                                ExpensesFragment.EXPENSES_FRAGMENT_TAG).commit()
//            }
//            R.id.nav_income -> {
//                val incomeFragment = IncomeFragment.newInstance("", "")
//                fragmentManager.beginTransaction().replace(R.id.layout_main_content, incomeFragment,
//                        IncomeFragment.INCOME_FRAGMENT_TAG).commit()
//            }
//            R.id.nav_statistics -> {
//                val statisticsFragment = StatisticsFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, statisticsFragment,
//                                StatisticsFragment.STATISTICS_FRAGMENT_TAG).commit()
//            }
//            R.id.nav_budgeting -> {
//                val budgetingFragment = BudgetingFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, budgetingFragment,
//                                BudgetingFragment.BUDGETING_FRAGMENT_TAG).commit()
//            }
//            R.id.nav_account_management -> {
//                val accountMgmtFragment = AccountMgmtFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, accountMgmtFragment,
//                                AccountMgmtFragment.ACCOUNT_MGMT_FRAGMENT_TAG).commit()
//            }
//            R.id.nav_accounting_notification -> {
//                val accountingNotificationFragment = AccountingNotificationFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, accountingNotificationFragment,
//                                AccountingNotificationFragment.ACCOUNTING_NOTIFICATION_FRAGMENT_TAG)
//                        .commit()
//            }
//            R.id.nav_currency_selection ->                 //TODO:
//                Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show()
//            R.id.nav_password_setting ->                 //TODO:
//                Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show()
//            R.id.nav_expenses_category_management -> {
//                val expensesCategoryMgmtFragment = ExpensesCategoryMgmtFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, expensesCategoryMgmtFragment,
//                                ExpensesCategoryMgmtFragment.EXPENSES_CATEGORY_MGMT_FRAGMENT_TAG)
//                        .commit()
//            }
//            R.id.nav_income_category_management -> {
//                val incomeCategoryMgmtFragment = IncomeCategoryMgmtFragment.newInstance("", "")
//                fragmentManager.beginTransaction()
//                        .replace(R.id.layout_main_content, incomeCategoryMgmtFragment,
//                                IncomeCategoryMgmtFragment.INCOME_CATEGORY_MGMT_FRAGMENT_TAG)
//                        .commit()
//            }
//            R.id.nav_export_file -> {
//                val baseDir = Environment.getExternalStorageDirectory().absolutePath
//                val fileName = "AccountingData.csv"
//                val filePath = baseDir + File.separator + fileName
//                val file = File(filePath)
//                var csvWriter: CSVWriter? = null
//                try {
//                    csvWriter = CSVWriter(FileWriter(file))
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                val db = MyDBHelper.getDatabase(this)
//                val expensesList = CsvUtil.getExpenseIncomeStringArrays(this,
//                        ExpensesDAO(db).allOrderByRecordTime,
//                        IncomeDAO(db).allOrderByRecordTime)
//                csvWriter?.writeAll(expensesList)
//                try {
//                    csvWriter?.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                val fileUri = Uri.fromFile(file)
//                val emailIntent = Intent(Intent.ACTION_SEND)
//                emailIntent.type = "message/rfc822"
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Accounting Data")
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Accounting Data Exporting")
//                // the attachment
//                emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
//                try {
//                    startActivity(Intent.createChooser(emailIntent, "Send mail"))
//                } catch (ex: ActivityNotFoundException) {
//                    Toast.makeText(this@MainActivity, "There are no email clients installed.",
//                            Toast.LENGTH_SHORT).show()
//                }
//            }
//            R.id.nav_cloud_backup ->                 //TODO:
//                Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show()
//            R.id.nav_use_teaching -> {
//                val intent = Intent(this@MainActivity, InstructionActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//            R.id.nav_about_us ->                 //TODO:
//                Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show()
//            R.id.nav_give_praise ->                 //TODO:
//                Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show()
//        }
//        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//        drawer.closeDrawer(GravityCompat.START)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        KeyboardUtil.closeKeyboard(this)
//        return super.onOptionsItemSelected(item)
//    }

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