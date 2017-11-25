package ogl4jo3.shaowei.ogl4jo3.utility.csv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.common.expenses.Expenses;
import ogl4jo3.shaowei.ogl4jo3.accounting.common.income.Income;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;

/**
 * CSV工具
 * Created by ogl4jo3 on 2017/11/25.
 */

public class CsvUtil {

	/**
	 * 取得支出、收入
	 * getExpenseIncomeStringArrays
	 */
	public static List<String[]> getExpenseIncomeStringArrays(Context mContext,
	                                                          List<Expenses> expensesList,
	                                                          List<Income> incomeList) {
		List<String[]> result = new ArrayList<>();
		result.addAll(expenseToStringArray(mContext, expensesList));
		result.addAll(incomeToStringArray(mContext, incomeList));
		return result;
	}

	/**
	 * 將expensesList轉換為listStringArray
	 */
	private static List<String[]> expenseToStringArray(Context mContext,
	                                                   List<Expenses> expensesList) {
		List<String[]> result = new ArrayList<>();

		// adding header record
		result.add(new String[]{"--Expense--"});
		result.add(new String[]{"RecordTime", "Account", "Category", "Price", "Description"});

		for (Expenses expenses : expensesList) {

			SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
			String categoryName =
					new CategoryDAO(db).getExpensesData(expenses.getCategoryId()).getName();
			result.add(
					new String[]{expenses.getRecordTime(), expenses.getAccountName(), categoryName,
							String.valueOf(expenses.getPrice()), expenses.getDescription()});
		}
		result.add(new String[]{""});
		result.add(new String[]{""});
		return result;
	}

	/**
	 * 將incomeList轉換為listStringArray
	 */
	private static List<String[]> incomeToStringArray(Context mContext, List<Income> incomeList) {
		List<String[]> result = new ArrayList<>();

		// adding header record
		result.add(new String[]{"--Income--"});
		result.add(new String[]{"RecordTime", "Account", "Category", "Price", "Description"});

		for (Income income : incomeList) {
			SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
			String categoryName =
					new CategoryDAO(db).getIncomeData(income.getCategoryId()).getName();
			result.add(new String[]{income.getRecordTime(), income.getAccountName(), categoryName,
					String.valueOf(income.getPrice()), income.getDescription()});
		}
		result.add(new String[]{""});
		result.add(new String[]{""});
		return result;
	}
}
