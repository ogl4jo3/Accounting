package com.ogl4jo3.accounting;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.ogl4jo3.accounting.common.expenses.Expenses;
import com.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.ogl4jo3.accounting.common.expenses.ExpensesFragment;
import com.ogl4jo3.accounting.common.expenses.ExpensesNewEditFragment;
import com.ogl4jo3.accounting.common.income.IncomeDAO;
import com.ogl4jo3.accounting.common.income.IncomeFragment;
import com.ogl4jo3.accounting.common.statistics.StatisticsFragment;
import com.ogl4jo3.accounting.setting.accountingnotification.AccountingNotificationFragment;
import com.ogl4jo3.accounting.setting.accountmanagement.AccountMgmtFragment;
import com.ogl4jo3.accounting.setting.budgeting.BudgetingFragment;
import com.ogl4jo3.accounting.setting.categorymanagement.expenses.ExpensesCategoryMgmtFragment;
import com.ogl4jo3.accounting.setting.categorymanagement.income.IncomeCategoryMgmtFragment;
import com.ogl4jo3.accounting.useteaching.UseTeachingActivity;
import com.ogl4jo3.accounting.utility.csv.CsvUtil;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;
import com.ogl4jo3.accounting.utility.date.DateUtil;
import com.ogl4jo3.accounting.utility.keyboard.KeyboardUtil;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	/**
	 * 雜湊密鑰For FaceBook
	 */
	private void printKeyHash() {
		try {
			@SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager()
					.getPackageInfo("ogl4jo3.shaowei" + ".ogl4jo3.accounting",
							PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				//Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		printKeyHash();
		setContentView(R.layout.activity_main);
		KeyboardUtil.setupUI(this, findViewById(R.id.drawer_layout));
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		FragmentManager fragmentManager = getFragmentManager();
		ExpensesFragment expensesFragment = ExpensesFragment.newInstance("", "");
		fragmentManager.beginTransaction().replace(R.id.layout_main_content, expensesFragment,
				ExpensesFragment.EXPENSES_FRAGMENT_TAG).commit();

		if (getIntent().getExtras().getInt(ExpensesDAO.CATEGORY_COLUMN) > 0) {//從記帳通知進入時
			//直接帶入類別、描述
			int categoryId = getIntent().getExtras().getInt(ExpensesDAO.CATEGORY_COLUMN);
			String description = getIntent().getExtras().getString(ExpensesDAO.DESCRIPTION_COLUMN);
			Expenses expenses = new Expenses();
			expenses.setId(-1);
			expenses.setCategoryId(categoryId);
			expenses.setDescription(description);
			ExpensesNewEditFragment expensesNewEditFragment = ExpensesNewEditFragment
					.newInstance(DateUtil.dateToStr(new Date()), new Gson().toJson(expenses));
			fragmentManager.beginTransaction()
					.replace(R.id.layout_main_content, expensesNewEditFragment, null)
					.addToBackStack(null).commit();
		}
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
			case R.id.nav_accounting_notification:
				AccountingNotificationFragment accountingNotificationFragment =
						AccountingNotificationFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountingNotificationFragment,
								AccountingNotificationFragment.ACCOUNTING_NOTIFICATION_FRAGMENT_TAG)
						.commit();
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
				String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
				String fileName = "AccountingData.csv";
				String filePath = baseDir + File.separator + fileName;
				File file = new File(filePath);

				CSVWriter csvWriter = null;
				try {
					csvWriter = new CSVWriter(new FileWriter(file));
				} catch (IOException e) {
					e.printStackTrace();
				}

				SQLiteDatabase db = MyDBHelper.getDatabase(this);
				List<String[]> expensesList = CsvUtil.getExpenseIncomeStringArrays(this,
						new ExpensesDAO(db).getAllOrderByRecordTime(),
						new IncomeDAO(db).getAllOrderByRecordTime());

				if (csvWriter != null) {
					csvWriter.writeAll(expensesList);
				}

				try {
					if (csvWriter != null) {
						csvWriter.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Uri fileUri = Uri.fromFile(file);

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Accounting Data");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Accounting Data Exporting");
				// the attachment
				emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
				try {
					startActivity(Intent.createChooser(emailIntent, "Send mail"));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(MainActivity.this, "There are no email clients installed.",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.nav_cloud_backup:
				//TODO:
				Toast.makeText(this, getString(R.string.msg_todo), Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_use_teaching:
				Intent intent = new Intent(MainActivity.this, UseTeachingActivity.class);
				startActivity(intent);
				finish();
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

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		KeyboardUtil.closeKeyboard(this);
		return super.onOptionsItemSelected(item);
	}
}
