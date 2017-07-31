package com.example.ogl4jo3.accounting.common.income;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ogl4jo3.utility.date.DateUtil;
import com.example.ogl4jo3.utility.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入 資料庫存取
 * Created by ogl4jo3 on 2017/7/26.
 */

public class IncomeDAO {

	// 表格名稱
	private static final String INCOME_TABLE_NAME = "income";

	// 編號表格欄位名稱，固定不變
	private static final String KEY_ID = "_id";
	// 表格欄位名稱
	private static final String PRICE_COLUMN = "price";
	private static final String CATEGORY_COLUMN = "category";
	private static final String DESCRIPTION_COLUMN = "description";
	private static final String RECORD_TIME_COLUMN = "recordTime";

	// CREATE_TABLE SQL指令
	public static final String CREATE_INCOME_TABLE =
			"CREATE TABLE IF NOT EXISTS " + INCOME_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + PRICE_COLUMN + " INTEGER ," +
					CATEGORY_COLUMN + " INTEGER," + DESCRIPTION_COLUMN + " VARCHAR ," +
					RECORD_TIME_COLUMN + " DATETIME NOT NULL);";
	// DROP_TABLE SQL指令
	public static final String DROP_INCOME_TABLE = "DROP TABLE IF EXISTS " + INCOME_TABLE_NAME;

	private final SQLiteDatabase database;

	/**
	 * 建構元
	 *
	 * @param database 資料庫
	 */
	public IncomeDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * 新增收入
	 *
	 * @param income 收入
	 * @return 新增成功
	 */
	public boolean newIncomeData(Income income) {
		// 建立準備新增資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(PRICE_COLUMN, income.getPrice());
		contentValues.put(CATEGORY_COLUMN, income.getCategoryId());
		contentValues.put(DESCRIPTION_COLUMN, income.getDescription());
		if (!StringUtil.isNullorEmpty(income.getRecordTime())) {
			contentValues.put(RECORD_TIME_COLUMN, income.getRecordTime());
		} else {
			contentValues.put(RECORD_TIME_COLUMN, DateUtil.getCurrentDate());
		}

		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		//database.close();
		return database.insert(INCOME_TABLE_NAME, null, contentValues) > 0;
	}

	/**
	 * 儲存原有資料
	 */
	public boolean saveIncomeData(Income income) {
		// 建立準備修改資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(PRICE_COLUMN, income.getPrice());
		contentValues.put(CATEGORY_COLUMN, income.getCategoryId());
		contentValues.put(DESCRIPTION_COLUMN, income.getDescription());

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		//String where = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(income.getId())};

		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(INCOME_TABLE_NAME, contentValues, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除
	 */
	public boolean delIncomeData(int id) {

		//String whereClause = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(id)};

		return database.delete(INCOME_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除類別時，同時刪除此類別所有紀錄
	 * 由類別ID刪除
	 */
	public static boolean delIncomeDataByCategory(SQLiteDatabase database, int categoryId) {
		String whereClause = CATEGORY_COLUMN + "=?";
		String[] whereArgs = {String.valueOf(categoryId)};

		return database.delete(INCOME_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 依照日期取得所有資料
	 *
	 * @return listincome
	 */
	public List<Income> getByDate(String date) {
		/*String selection=RECORD_TIME_COLUMN+"=?";
		String[] selectionArgs={date};
		String orderBy = RECORD_TIME_COLUMN + " ASC";
		List<Income> result = new ArrayList<>();
		Cursor cursor = database.query(INCOME_TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getIncome(cursor));
		}*/

		List<Income> result = new ArrayList<>();
		String queryString =
				"SELECT * FROM " + INCOME_TABLE_NAME + " WHERE " + RECORD_TIME_COLUMN + "=?" +
						"ORDER BY " + KEY_ID + " ASC";
		Cursor cursor = database.rawQuery(queryString, new String[]{date});

		while (cursor.moveToNext()) {
			result.add(getIncome(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * 取得所有資料
	 *
	 * @return listIncome
	 */
	public List<Income> getAll() {
		String orderBy = KEY_ID + " ASC";
		List<Income> result = new ArrayList<>();
		Cursor cursor = database.query(INCOME_TABLE_NAME, null, null, null, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getIncome(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * Cursor目前的資料包裝為物件
	 *
	 * @param cursor Cursor
	 * @return Income
	 */
	private Income getIncome(Cursor cursor) {
		// 準備回傳結果用的物件
		Income result = new Income();

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
	public void newTestIncomeData() {
		Income income = new Income();
		income.setPrice(10);
		income.setCategoryId(12);
		income.setDescription("1asd0001");
		Income income_1 = new Income();
		income_1.setPrice(10);
		income_1.setCategoryId(1);
		income_1.setDescription("asd0001");
		Income income_2 = new Income();
		income_2.setPrice(50);
		income_2.setCategoryId(2);
		income_2.setDescription("qwe1234");
		income_2.setRecordTime("2017-07-25");
		Income income_3 = new Income();
		income_3.setPrice(98);
		income_3.setCategoryId(3);
		income_3.setDescription("12345fasdf");
		income_3.setRecordTime("2017-07-26");
		Income income_4 = new Income();
		income_4.setPrice(98);
		income_4.setCategoryId(4);
		income_4.setDescription("12345fasdf");
		income_4.setRecordTime("2017-07-28");
		Income income_5 = new Income();
		income_5.setPrice(98);
		income_5.setCategoryId(5);
		income_5.setDescription("12345fasdf");
		income_5.setRecordTime("2017-07-29");
		Income income_6 = new Income();
		income_6.setPrice(98);
		income_6.setCategoryId(6);
		income_6.setDescription("12345fasdf");
		income_6.setRecordTime("2017-09-29");
		newIncomeData(income);
		newIncomeData(income_1);
		newIncomeData(income_2);
		newIncomeData(income_3);
		newIncomeData(income_4);
		newIncomeData(income_5);
		newIncomeData(income_6);
	}
}
