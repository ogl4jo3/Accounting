package com.example.ogl4jo3.accounting;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ogl4jo3.accounting.common.expenses.ExpensesFragment;
import com.example.ogl4jo3.accounting.common.income.IncomeFragment;
import com.example.ogl4jo3.accounting.common.statistics.StatisticsFragment;
import com.example.ogl4jo3.accounting.setting.accountmanagement.AccountMgmtFragment;
import com.example.ogl4jo3.accounting.setting.budgeting.BudgetingFragment;
import com.example.ogl4jo3.accounting.setting.categorymanagement.expenses.ExpensesCategoryMgmtFragment;
import com.example.ogl4jo3.accounting.setting.categorymanagement.income.IncomeCategoryMgmtFragment;
import com.example.ogl4jo3.utility.keyboard.KeyboardUtil;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		KeyboardUtil.setupUI(this, findViewById(R.id.drawer_layout));
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle =
				new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
						R.string.navigation_drawer_close) {

					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {
						KeyboardUtil.closeKeyboard(MainActivity.this);
						super.onDrawerSlide(drawerView, slideOffset);
					}
				};
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		FragmentManager fragmentManager = getFragmentManager();
		ExpensesFragment expensesFragment = ExpensesFragment.newInstance("", "");
		fragmentManager.beginTransaction().replace(R.id.layout_main_content, expensesFragment,
				ExpensesFragment.EXPENSES_FRAGMENT_TAG).commit();
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if (fragmentManager.getBackStackEntryCount() > 0) {
				fragmentManager.popBackStack();
			} else {
				super.onBackPressed();
			}
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStackImmediate(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE); //消除其他堆疊的Fragment

		switch (id) {
			// 常用
			case R.id.nav_expenses:
				ExpensesFragment expensesFragment = ExpensesFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, expensesFragment,
								ExpensesFragment.EXPENSES_FRAGMENT_TAG).commit();
				break;
			case R.id.nav_income:
				IncomeFragment incomeFragment = IncomeFragment.newInstance("", "");
				fragmentManager.beginTransaction().replace(R.id.layout_main_content, incomeFragment,
						IncomeFragment.INCOME_FRAGMENT_TAG).commit();
				break;
			case R.id.nav_statistics:
				StatisticsFragment statisticsFragment = StatisticsFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, statisticsFragment,
								StatisticsFragment.STATISTICS_FRAGMENT_TAG).commit();
				break;
			//設定
			case R.id.nav_budgeting:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				BudgetingFragment budgetingFragment = BudgetingFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, budgetingFragment,
								BudgetingFragment.BUDGETING_FRAGMENT_TAG).commit();
				break;
			case R.id.nav_account_management:
				AccountMgmtFragment accountMgmtFragment = AccountMgmtFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountMgmtFragment,
								AccountMgmtFragment.ACCOUNT_MGMT_FRAGMENT_TAG).commit();
				break;
			case R.id.nav_currency_selection:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_password_setting:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_expenses_category_management:
				ExpensesCategoryMgmtFragment expensesCategoryMgmtFragment =
						ExpensesCategoryMgmtFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, expensesCategoryMgmtFragment,
								ExpensesCategoryMgmtFragment.EXPENSES_CATEGORY_MGMT_FRAGMENT_TAG)
						.commit();
				break;

			case R.id.nav_income_category_management:
				IncomeCategoryMgmtFragment incomeCategoryMgmtFragment =
						IncomeCategoryMgmtFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, incomeCategoryMgmtFragment,
								IncomeCategoryMgmtFragment.INCOME_CATEGORY_MGMT_FRAGMENT_TAG)
						.commit();
				break;
			// 其他
			case R.id.nav_export_file:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_cloud_backup:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_about_us:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_give_praise:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		KeyboardUtil.closeKeyboard(this);
		return super.onOptionsItemSelected(item);
	}
}
