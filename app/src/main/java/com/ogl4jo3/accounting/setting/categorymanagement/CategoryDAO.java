package com.ogl4jo3.accounting.setting.categorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.ogl4jo3.accounting.common.income.IncomeDAO;
import com.ogl4jo3.accounting.setting.accountingnotification.AccountingNotificationDAO;

import static android.content.ContentValues.TAG;

/**
 * 類別資料庫存取
 * Created by ogl4jo3 on 2017/7/14.
 */
@Deprecated
public class CategoryDAO {

	// 表格名稱
	private static final String EXPENSES_CATEGORY_TABLE_NAME = "expensesCategory";
	private static final String INCOME_CATEGORY_TABLE_NAME = "incomeCategory";

	// 編號表格欄位名稱，固定不變
	private static final String KEY_ID = "_id";
	// 表格欄位名稱
	private static final String NAME_COLUMN = "name";
	private static final String ICON_COLUMN = "icon";
	private static final String ORDER_NUM_COLUMN = "orderNum";

	// CREATE_TABLE SQL指令
	public static final String CREATE_EXPENSES_CATEGORY_TABLE =
			"CREATE TABLE IF NOT EXISTS " + EXPENSES_CATEGORY_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COLUMN + " VARCHAR ," +
					ICON_COLUMN + " INTEGER," + ORDER_NUM_COLUMN + " INTEGER);";
	public static final String CREATE_INCOME_CATEGORY_TABLE =
			"CREATE TABLE IF NOT EXISTS " + INCOME_CATEGORY_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COLUMN + " VARCHAR ," +
					ICON_COLUMN + " INTEGER," + ORDER_NUM_COLUMN + " INTEGER);";
	// DROP_TABLE SQL指令
	public static final String DROP_EXPENSES_CATEGORY_TABLE =
			"DROP TABLE IF EXISTS " + EXPENSES_CATEGORY_TABLE_NAME;
	public static final String DROP_INCOME_CATEGORY_TABLE =
			"DROP TABLE IF EXISTS " + INCOME_CATEGORY_TABLE_NAME;

	private final SQLiteDatabase database;

	/**
	 * 建構元
	 *
	 * @param database 資料庫
	 */
	public CategoryDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * 新增支出類別
	 *
	 * @param category 類別
	 * @return 新增成功
	 */
	public boolean newExpensesData(Category category) {
		return newData(EXPENSES_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 新增收入類別
	 *
	 * @param category 類別
	 * @return 新增成功
	 */
	public boolean newIncomeData(Category category) {
		return newData(INCOME_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 儲存支出類別
	 *
	 * @param category 類別
	 * @return 儲存成功
	 */
	public boolean saveExpensesData(Category category) {
		return saveData(EXPENSES_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 儲存收入類別
	 *
	 * @param category 類別
	 * @return 儲存成功
	 */
	public boolean saveIncomeData(Category category) {
		return saveData(INCOME_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 根據指定ID取得支出資料
	 *
	 * @param id 指定ID
	 * @return Category
	 */
	public Category getExpensesData(int id) {
		return getData(EXPENSES_CATEGORY_TABLE_NAME, id);
	}

	/**
	 * 根據指定ID取得收入資料
	 *
	 * @param id 指定ID
	 * @return Category
	 */
	public Category getIncomeData(int id) {
		return getData(INCOME_CATEGORY_TABLE_NAME, id);
	}

	/**
	 * 刪除支出類別
	 *
	 * @param category 類別
	 * @return 刪除成功
	 */
	public boolean deleteExpensesData(Category category) {
		//刪除類別時，同時刪除此類別所有支出紀錄
		ExpensesDAO.delExpensesDataByCategory(database, category.getId());
		//刪除類別時，同時刪除此類別記帳通知
		AccountingNotificationDAO.delAccountNotificationByCategory(database, category.getId());
		return deleteData(EXPENSES_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 刪除收入類別
	 *
	 * @param category 類別
	 * @return 刪除成功
	 */
	public boolean deleteIncomeData(Category category) {
		//刪除類別時，同時刪除此類別所有收入紀錄
		IncomeDAO.delIncomeDataByCategory(database, category.getId());
		return deleteData(INCOME_CATEGORY_TABLE_NAME, category);
	}

	/**
	 * 更新支出類別排列順序
	 *
	 * @param category_1 Category_1
	 * @param category_2 Category_2
	 * @return 成功?
	 */
	public boolean updateExpensesOrderNum(Category category_1, Category category_2) {
		return updateOrderNum(EXPENSES_CATEGORY_TABLE_NAME, category_1, category_2);
	}

	/**
	 * 更新收入類別排列順序
	 *
	 * @param category_1 Category_1
	 * @param category_2 Category_2
	 * @return 成功?
	 */
	public boolean updateIncomeOrderNum(Category category_1, Category category_2) {
		return updateOrderNum(INCOME_CATEGORY_TABLE_NAME, category_1, category_2);
	}

	/**
	 * 取得所有支出類別
	 *
	 * @return 所有支出類別
	 */
	public List<Category> getAllExpensesCategories() {
		return getAll(EXPENSES_CATEGORY_TABLE_NAME);
	}

	/**
	 * 取得所有收入類別
	 *
	 * @return 所有收入類別
	 */
	public List<Category> getAllIncomeCategories() {
		return getAll(INCOME_CATEGORY_TABLE_NAME);
	}

	/**
	 * 檢查支出類別是否重複
	 *
	 * @param categoryName 需要檢查的名稱
	 * @param categoryIcon 需要檢查的Icon
	 * @param categoryID   不包含本身；若此數字為-1代表新增，則搜尋全部
	 * @return 重複回傳true
	 */
	public boolean checkExpensesRepeated(String categoryName, int categoryIcon, int categoryID) {
		return checkRepeated(EXPENSES_CATEGORY_TABLE_NAME, categoryName, categoryIcon, categoryID);
	}

	/**
	 * 檢查收入類別是否重複
	 *
	 * @param categoryName 需要檢查的名稱
	 * @param categoryIcon 需要檢查的Icon
	 * @param categoryID   不包含本身；若此數字為-1代表新增，則搜尋全部
	 * @return 重複回傳true
	 */
	public boolean checkIncomeRepeated(String categoryName, int categoryIcon, int categoryID) {
		return checkRepeated(INCOME_CATEGORY_TABLE_NAME, categoryName, categoryIcon, categoryID);
	}

	/**
	 * 新增
	 */
	private boolean newData(String TABLE_NAME, Category category) {
		// 建立準備新增資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, category.getName());
		contentValues.put(ICON_COLUMN, category.getIcon());
		contentValues.put(ORDER_NUM_COLUMN, getMaxOrderNum(TABLE_NAME) + 1);

		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		//database.close();
		return database.insert(TABLE_NAME, null, contentValues) > 0;
	}

	/**
	 * 儲存原有資料
	 */
	private boolean saveData(String TABLE_NAME, Category category) {
		// 建立準備修改資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, category.getName());
		contentValues.put(ICON_COLUMN, category.getIcon());
		contentValues.put(ORDER_NUM_COLUMN, category.getOrderNum());

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		//String where = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(category.getId())};

		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(TABLE_NAME, contentValues, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除類別
	 *
	 * @param TABLE_NAME 表格名稱
	 * @param category   Category
	 * @return 成功?
	 */
	private boolean deleteData(String TABLE_NAME, Category category) {

		//String whereClause = ORDER_NUM_COLUMN + "=" + category.getOrderNum();
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(category.getId())};

		return database.delete(TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 根據指定ID取得資料
	 *
	 * @return Category
	 */
	private Category getData(String TABLE_NAME, int id) {
		Category result = new Category();
		String selection = KEY_ID + "=?";
		String[] selectionArgs = {String.valueOf(id)};
		Cursor cursor =
				database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

		if (cursor.moveToNext()) {
			result = getCategory(cursor);
		}

		cursor.close();
		return result;
	}

	/**
	 * 取得所有資料
	 *
	 * @return listCategory
	 */
	private List<Category> getAll(String TABLE_NAME) {
		String orderBy = ORDER_NUM_COLUMN + " ASC";
		List<Category> result = new ArrayList<>();
		Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getCategory(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * 更新排列順序
	 *
	 * @param TABLE_NAME 表格名稱
	 * @param category_1 Category_1
	 * @param category_2 Category_2
	 * @return 成功?
	 */
	private boolean updateOrderNum(String TABLE_NAME, Category category_1, Category category_2) {

		ContentValues cv_1 = new ContentValues();
		cv_1.put(ORDER_NUM_COLUMN, category_2.getOrderNum());

		ContentValues cv_2 = new ContentValues();
		cv_2.put(ORDER_NUM_COLUMN, category_1.getOrderNum());

		String whereClause = KEY_ID + "=?";
		String[] whereValue_1 = {String.valueOf(category_1.getId())};
		String[] whereValue_2 = {String.valueOf(category_2.getId())};

		Log.d(TAG, "category_1:" + category_1.toString());
		Log.d(TAG, "category_2:" + category_2.toString());

		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(TABLE_NAME, cv_1, whereClause, whereValue_1) > 0 &
				database.update(TABLE_NAME, cv_2, whereClause, whereValue_2) > 0;
	}

	/**
	 * 取得目前最大排序
	 *
	 * @param TABLE_NAME 表格名稱
	 * @return 最大排序
	 */
	private int getMaxOrderNum(String TABLE_NAME) {
		int result = 0;
		Cursor cursor =
				database.rawQuery("SELECT MAX(" + ORDER_NUM_COLUMN + ") FROM " + TABLE_NAME, null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();

		return result;
	}

	/**
	 * 檢查類別是否重複
	 *
	 * @param TABLE_NAME   表格名稱
	 * @param categoryName 需要檢查的名稱
	 * @param categoryIcon 需要檢查的Icon
	 * @param categoryID   不包含本身；若此數字為-1代表新增，則搜尋全部
	 * @return 重複
	 */
	private boolean checkRepeated(String TABLE_NAME, String categoryName, int categoryIcon,
	                              int categoryID) {
		boolean isRepeated = false;
		Cursor cursor;
		//categoryID == -1，為新增，搜尋全部是否有重複
		String queryString =
				"SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN + "=? AND " +
						ICON_COLUMN + "=?";

		if (categoryID == -1) { //新增
			String[] selectionArgs = {categoryName, String.valueOf(categoryIcon)};
			cursor = database.rawQuery(queryString, selectionArgs);
		} else {    //編輯，categoryID
			queryString += " AND " + KEY_ID + "!=?";
			String[] selectionArgs =
					{categoryName, String.valueOf(categoryIcon), String.valueOf(categoryID)};
			cursor = database.rawQuery(queryString, selectionArgs);
		}

		if (cursor.moveToNext()) {
			isRepeated = cursor.getInt(0) >= 1;
		}
		cursor.close();

		return isRepeated;

	}
	/*
	/**
	 * 取得資料數量
	 *
	 * @return 資料數量
	 */
	/*private int getCount(String TABLE_NAME) {
		int result = 0;
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();

		return result;
	}*/

	/**
	 * Cursor目前的資料包裝為物件
	 *
	 * @param cursor Cursor
	 * @return Category
	 */
	private Category getCategory(Cursor cursor) {
		// 準備回傳結果用的物件
		Category result = new Category();

		result.setId(cursor.getInt(0));
		result.setName(cursor.getString(1));
		result.setIcon(cursor.getInt(2));
		result.setOrderNum(cursor.getInt(3));

		// 回傳結果
		return result;
	}

	/**
	 * 初始化支出類別資料
	 *
	 * @param database SQLiteDatabase
	 */
	public static void initExpensesData(Context context, SQLiteDatabase database) {
		String insertInto =
				"insert into " + EXPENSES_CATEGORY_TABLE_NAME + "(" + NAME_COLUMN + "," +
						ICON_COLUMN + "," + ORDER_NUM_COLUMN + ") ";
		String[] initName = {context.getString(com.ogl4jo3.accounting.R.string.category_lunch),
				context.getString(com.ogl4jo3.accounting.R.string.category_dinner),
				context.getString(com.ogl4jo3.accounting.R.string.category_afternoon_tea),
				context.getString(com.ogl4jo3.accounting.R.string.category_dessert),
				context.getString(com.ogl4jo3.accounting.R.string.category_fitness),
				context.getString(com.ogl4jo3.accounting.R.string.category_house),
				context.getString(com.ogl4jo3.accounting.R.string.category_house_1),
				context.getString(com.ogl4jo3.accounting.R.string.category_gift),
				context.getString(com.ogl4jo3.accounting.R.string.category_medical),
				context.getString(com.ogl4jo3.accounting.R.string.category_parenting),
				context.getString(com.ogl4jo3.accounting.R.string.category_pet),
				context.getString(com.ogl4jo3.accounting.R.string.category_phone),
				context.getString(com.ogl4jo3.accounting.R.string.category_refueling),
				context.getString(com.ogl4jo3.accounting.R.string.category_swim),
				context.getString(com.ogl4jo3.accounting.R.string.category_traffic),
				context.getString(com.ogl4jo3.accounting.R.string.category_video_game),
				context.getString(com.ogl4jo3.accounting.R.string.category_other)};
		int[] initIcon = {
				com.ogl4jo3.accounting.R.drawable.ic_category_lunch, com.ogl4jo3.accounting.R.drawable.ic_category_dinner,
				com.ogl4jo3.accounting.R.drawable.ic_category_afternoon_tea, com.ogl4jo3.accounting.R.drawable.ic_category_dessert,
				com.ogl4jo3.accounting.R.drawable.ic_category_fitness, com.ogl4jo3.accounting.R.drawable.ic_category_house,
				com.ogl4jo3.accounting.R.drawable.ic_category_house_1, com.ogl4jo3.accounting.R.drawable.ic_category_gift,
				com.ogl4jo3.accounting.R.drawable.ic_category_medical, com.ogl4jo3.accounting.R.drawable.ic_category_parenting,
				com.ogl4jo3.accounting.R.drawable.ic_category_pet, com.ogl4jo3.accounting.R.drawable.ic_category_phone,
				com.ogl4jo3.accounting.R.drawable.ic_category_refueling, com.ogl4jo3.accounting.R.drawable.ic_category_swim,
				com.ogl4jo3.accounting.R.drawable.ic_category_traffic, com.ogl4jo3.accounting.R.drawable.ic_category_video_game,
				com.ogl4jo3.accounting.R.drawable.ic_category_other};
		for (int i = 0; i < initName.length; i++) {
			database.execSQL(
					insertInto + " values('" + initName[i] + "','" + initIcon[i] + "','" + i +
							"')");
		}
	}

	/**
	 * 初始化收入類別資料
	 *
	 * @param database SQLiteDatabase
	 */
	public static void initIncomeData(Context context, SQLiteDatabase database) {
		String insertInto = "insert into " + INCOME_CATEGORY_TABLE_NAME + "(" + NAME_COLUMN + "," +
				ICON_COLUMN + "," + ORDER_NUM_COLUMN + ") ";
		String[] initName = {context.getString(com.ogl4jo3.accounting.R.string.category_salary),
				context.getString(com.ogl4jo3.accounting.R.string.category_bonus),
				context.getString(com.ogl4jo3.accounting.R.string.category_investment),
				context.getString(com.ogl4jo3.accounting.R.string.category_subsidy),
				context.getString(com.ogl4jo3.accounting.R.string.category_other)};
		int[] initIcon = {
				com.ogl4jo3.accounting.R.drawable.ic_category_salary, com.ogl4jo3.accounting.R.drawable.ic_category_bonus,
				com.ogl4jo3.accounting.R.drawable.ic_category_investment, com.ogl4jo3.accounting.R.drawable.ic_category_subsidy,
				com.ogl4jo3.accounting.R.drawable.ic_category_other};
		for (int i = 0; i < initName.length; i++) {
			database.execSQL(
					insertInto + " values('" + initName[i] + "','" + initIcon[i] + "','" + i +
							"')");
		}
	}
}
