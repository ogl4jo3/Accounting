package com.example.ogl4jo3.accounting.common.expenses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ogl4jo3.utility.date.DateUtil;
import com.example.ogl4jo3.utility.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 支出 資料庫存取
 * Created by ogl4jo3 on 2017/7/26.
 */

public class ExpensesDAO {

	// 表格名稱
	private static final String EXPENSES_TABLE_NAME = "expenses";

	// 編號表格欄位名稱，固定不變
	private static final String KEY_ID = "_id";
	// 表格欄位名稱
	private static final String PRICE_COLUMN = "price";
	private static final String CATEGORY_COLUMN = "category";
	private static final String DESCRIPTION_COLUMN = "description";
	private static final String RECORD_TIME_COLUMN = "recordTime";

	// CREATE_TABLE SQL指令
	public static final String CREATE_EXPENSES_TABLE =
			"CREATE TABLE IF NOT EXISTS " + EXPENSES_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + PRICE_COLUMN + " INTEGER ," +
					CATEGORY_COLUMN + " INTEGER," + DESCRIPTION_COLUMN + " VARCHAR ," +
					RECORD_TIME_COLUMN + " DATETIME NOT NULL);";
	// DROP_TABLE SQL指令
	public static final String DROP_EXPENSES_TABLE = "DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME;

	private final SQLiteDatabase database;

	/**
	 * 建構元
	 *
	 * @param database 資料庫
	 */
	public ExpensesDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * 新增支出
	 *
	 * @param expenses 支出
	 * @return 新增成功
	 */
	public boolean newExpensesData(Expenses expenses) {
		// 建立準備新增資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(PRICE_COLUMN, expenses.getPrice());
		contentValues.put(CATEGORY_COLUMN, expenses.getCategoryId());
		contentValues.put(DESCRIPTION_COLUMN, expenses.getDescription());
		if (!StringUtil.isNullorEmpty(expenses.getRecordTime())) {
			contentValues.put(RECORD_TIME_COLUMN, expenses.getRecordTime());
		} else {
			contentValues.put(RECORD_TIME_COLUMN, DateUtil.getCurrentDate());
		}

		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		//database.close();
		return database.insert(EXPENSES_TABLE_NAME, null, contentValues) > 0;
	}

	/**
	 * 儲存原有資料
	 */
	public boolean saveExpensesData(Expenses expenses) {
		// 建立準備修改資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(PRICE_COLUMN, expenses.getPrice());
		contentValues.put(CATEGORY_COLUMN, expenses.getCategoryId());
		contentValues.put(DESCRIPTION_COLUMN, expenses.getDescription());

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		//String where = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(expenses.getId())};

		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(EXPENSES_TABLE_NAME, contentValues, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除
	 */
	public boolean delExpensesData(int id) {

		//String whereClause = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(id)};

		return database.delete(EXPENSES_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除類別時，同時刪除此類別所有紀錄
	 * 由類別ID刪除
	 */
	public static boolean delExpensesDataByCategory(SQLiteDatabase database, int categoryId) {
		String whereClause = CATEGORY_COLUMN + "=?";
		String[] whereArgs = {String.valueOf(categoryId)};

		return database.delete(EXPENSES_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 依照日期取得所有資料
	 *
	 * @return listExpenses
	 */
	public List<Expenses> getByDate(String date) {
		/*String selection=RECORD_TIME_COLUMN+"=?";
		String[] selectionArgs={date};
		String orderBy = RECORD_TIME_COLUMN + " ASC";
		List<Expenses> result = new ArrayList<>();
		Cursor cursor = database.query(EXPENSES_TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getExpenses(cursor));
		}*/

		List<Expenses> result = new ArrayList<>();
		String queryString =
				"SELECT * FROM " + EXPENSES_TABLE_NAME + " WHERE " + RECORD_TIME_COLUMN + "=?" +
						"ORDER BY " + KEY_ID + " ASC";
		Cursor cursor = database.rawQuery(queryString, new String[]{date});

		while (cursor.moveToNext()) {
			result.add(getExpenses(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * 取得所有資料
	 *
	 * @return listExpenses
	 */
	public List<Expenses> getAll() {
		String orderBy = KEY_ID + " ASC";
		List<Expenses> result = new ArrayList<>();
		Cursor cursor = database.query(EXPENSES_TABLE_NAME, null, null, null, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getExpenses(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * Cursor目前的資料包裝為物件
	 *
	 * @param cursor Cursor
	 * @return Expenses
	 */
	private Expenses getExpenses(Cursor cursor) {
		// 準備回傳結果用的物件
		Expenses result = new Expenses();

		result.setId(cursor.getInt(0));
		result.setPrice(cursor.getInt(1));
		result.setCategoryId(cursor.getInt(2));
		result.setDescription(cursor.getString(3));
		result.setRecordTime(cursor.getString(4));

		// 回傳結果
		return result;
	}

	/**
	 * 新增測試用資料
	 */
	public void newTestExpensesData() {
		Expenses expenses = new Expenses();
		expenses.setPrice(10);
		expenses.setCategoryId(12);
		expenses.setDescription("1asd0001");
		Expenses expenses_1 = new Expenses();
		expenses_1.setPrice(10);
		expenses_1.setCategoryId(1);
		expenses_1.setDescription("asd0001");
		Expenses expenses_2 = new Expenses();
		expenses_2.setPrice(50);
		expenses_2.setCategoryId(2);
		expenses_2.setDescription("qwe1234");
		expenses_2.setRecordTime("2017-07-25");
		Expenses expenses_3 = new Expenses();
		expenses_3.setPrice(98);
		expenses_3.setCategoryId(3);
		expenses_3.setDescription("12345fasdf");
		expenses_3.setRecordTime("2017-07-26");
		Expenses expenses_4 = new Expenses();
		expenses_4.setPrice(98);
		expenses_4.setCategoryId(4);
		expenses_4.setDescription("12345fasdf");
		expenses_4.setRecordTime("2017-07-28");
		Expenses expenses_5 = new Expenses();
		expenses_5.setPrice(98);
		expenses_5.setCategoryId(5);
		expenses_5.setDescription("12345fasdf");
		expenses_5.setRecordTime("2017-07-29");
		Expenses expenses_6 = new Expenses();
		expenses_6.setPrice(98);
		expenses_6.setCategoryId(6);
		expenses_6.setDescription("12345fasdf");
		expenses_6.setRecordTime("2017-09-29");
		newExpensesData(expenses);
		newExpensesData(expenses_1);
		newExpensesData(expenses_2);
		newExpensesData(expenses_3);
		newExpensesData(expenses_4);
		newExpensesData(expenses_5);
		newExpensesData(expenses_6);
	}
}
