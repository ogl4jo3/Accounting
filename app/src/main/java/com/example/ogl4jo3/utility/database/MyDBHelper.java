package com.example.ogl4jo3.utility.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.example.ogl4jo3.accounting.common.income.IncomeDAO;
import com.example.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;

/**
 * 資料庫
 * Created by ogl4jo3 on 2017/7/13.
 */

public class MyDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "mydata"; // 資料庫名稱
	private static final int DATABASE_VERSION = 49; // 版本,此一數字一改(不管變大變小),資料即刪並重建
	private static SQLiteDatabase database;
	private Context context;

	public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
	                  int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	public static SQLiteDatabase getDatabase(Context context) {
		if (database == null || !database.isOpen()) {
			database = new MyDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
					.getWritableDatabase();
		}

		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

		// 建立應用程式需要的表格
		sqLiteDatabase.execSQL(CategoryDAO.CREATE_EXPENSES_CATEGORY_TABLE);
		sqLiteDatabase.execSQL(CategoryDAO.CREATE_INCOME_CATEGORY_TABLE);
		sqLiteDatabase.execSQL(ExpensesDAO.CREATE_EXPENSES_TABLE);
		sqLiteDatabase.execSQL(IncomeDAO.CREATE_INCOME_TABLE);
		// 初始化資料
		CategoryDAO.initExpensesData(context, sqLiteDatabase);//支出類別
		CategoryDAO.initIncomeData(context, sqLiteDatabase);//收入類別

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		// 刪除原有的表格
		sqLiteDatabase.execSQL(CategoryDAO.DROP_EXPENSES_CATEGORY_TABLE);
		sqLiteDatabase.execSQL(CategoryDAO.DROP_INCOME_CATEGORY_TABLE);
		sqLiteDatabase.execSQL(ExpensesDAO.DROP_EXPENSES_TABLE);
		sqLiteDatabase.execSQL(IncomeDAO.DROP_INCOME_TABLE);
		// 呼叫onCreate建立新版的表格
		onCreate(sqLiteDatabase);

	}
}
