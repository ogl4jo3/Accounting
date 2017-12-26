package ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountingnotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;

/**
 * 記帳通知DAO
 * Created by ogl4jo3 on 2017/12/18.
 */

public class AccountingNotificationDAO {

	// 表格名稱
	private static final String ACCOUNTING_NOTIFICATION_TABLE_NAME = "accountingNotification";

	// 編號表格欄位名稱，固定不變
	public static final String KEY_ID = "_id";
	// 表格欄位名稱
	private static final String NAME_COLUMN = "name";
	private static final String CATEGORY_ID_COLUMN = "categoryId";
	private static final String CATEGORY_NAME_COLUMN = "categoryName";
	private static final String CATEGORY_ICON_COLUMN = "categoryIcon";
	private static final String NOTIFICATION_HOUR_COLUMN = "notificationHour";
	private static final String NOTIFICATION_MINUTE_COLUMN = "notificationMinute";
	private static final String IS_ON_COLUMN = "isOn";

	// CREATE_TABLE SQL指令
	public static final String CREATE_ACCOUNTING_NOTIFICATION_TABLE =
			"CREATE TABLE IF NOT EXISTS " + ACCOUNTING_NOTIFICATION_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COLUMN + " TEXT NOT NULL," +
					CATEGORY_ID_COLUMN + " INTEGER NOT NULL," + CATEGORY_NAME_COLUMN +
					" TEXT NOT NULL," + CATEGORY_ICON_COLUMN + " INTEGER NOT NULL," +
					NOTIFICATION_HOUR_COLUMN + " INTEGER NOT NULL," + NOTIFICATION_MINUTE_COLUMN +
					" INTEGER NOT NULL," + IS_ON_COLUMN + " INTEGER NOT NULL);";
	// DROP_TABLE SQL指令
	public static final String DROP_ACCOUNTING_NOTIFICATION_TABLE =
			"DROP TABLE IF EXISTS " + ACCOUNTING_NOTIFICATION_TABLE_NAME;

	private final SQLiteDatabase database;

	/**
	 * 建構元
	 *
	 * @param database 資料庫
	 */
	public AccountingNotificationDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * 新增記帳通知
	 *
	 * @param accountingNotification 記帳通知
	 * @return 新增成功
	 */
	public boolean newAccountingNotification(AccountingNotification accountingNotification) {
		// 建立準備新增資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, accountingNotification.getNotificationName());
		contentValues.put(CATEGORY_ID_COLUMN, accountingNotification.getCategoryId());
		contentValues.put(CATEGORY_NAME_COLUMN, accountingNotification.getCategoryName());
		contentValues.put(CATEGORY_ICON_COLUMN, accountingNotification.getCategoryIcon());
		contentValues.put(NOTIFICATION_HOUR_COLUMN, accountingNotification.getNotificationHour());
		contentValues
				.put(NOTIFICATION_MINUTE_COLUMN, accountingNotification.getNotificationMinute());
		contentValues.put(IS_ON_COLUMN, accountingNotification.isOn() ? 1 : 0);

		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		return database.insert(ACCOUNTING_NOTIFICATION_TABLE_NAME, null, contentValues) > 0;
	}

	/**
	 * 儲存原有資料
	 */
	public boolean saveData(AccountingNotification accountingNotification) {
		// 建立準備修改資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, accountingNotification.getNotificationName());
		contentValues.put(CATEGORY_ID_COLUMN, accountingNotification.getCategoryId());
		contentValues.put(CATEGORY_NAME_COLUMN, accountingNotification.getCategoryName());
		contentValues.put(CATEGORY_ICON_COLUMN, accountingNotification.getCategoryIcon());
		contentValues.put(NOTIFICATION_HOUR_COLUMN, accountingNotification.getNotificationHour());
		contentValues
				.put(NOTIFICATION_MINUTE_COLUMN, accountingNotification.getNotificationMinute());
		contentValues.put(IS_ON_COLUMN, accountingNotification.isOn() ? 1 : 0);

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(accountingNotification.getId())};

		// 執行修改資料並回傳修改的資料數量是否成功
		return database
				.update(ACCOUNTING_NOTIFICATION_TABLE_NAME, contentValues, whereClause, whereArgs) >
				0;
	}

	/**
	 * 儲存通知開關
	 */
	public boolean saveOnOff(AccountingNotification accountingNotification, boolean isOn) {

		ContentValues cv = new ContentValues();
		cv.put(IS_ON_COLUMN, isOn ? 1 : 0);
		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(ACCOUNTING_NOTIFICATION_TABLE_NAME, cv, KEY_ID + "=?",
				new String[]{String.valueOf(accountingNotification.getId())}) > 0;
	}

	/**
	 * 刪除
	 */
	public boolean delAccountNotification(AccountingNotification accountingNotification) {
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(accountingNotification.getId())};

		return database.delete(ACCOUNTING_NOTIFICATION_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除
	 * 類別被刪除時，同時刪除此類別的記帳通知
	 * 在CategoryDAO使用到，所以為static
	 */
	public static boolean delAccountNotificationByCategory(SQLiteDatabase database,
	                                                       int categoryId) {
		String whereClause = CATEGORY_ID_COLUMN + "=?";
		String[] whereArgs = {String.valueOf(categoryId)};

		return database.delete(ACCOUNTING_NOTIFICATION_TABLE_NAME, whereClause, whereArgs) > 0;
	}

	/**
	 * 依照ID取得記帳通知
	 *
	 * @return AccountingNotification
	 */
	public AccountingNotification getById(int accountNotificationId) {
		AccountingNotification result = new AccountingNotification();

		String queryString =
				"SELECT * FROM " + ACCOUNTING_NOTIFICATION_TABLE_NAME + " WHERE " + KEY_ID + "=?";
		Cursor cursor =
				database.rawQuery(queryString, new String[]{String.valueOf(accountNotificationId)});

		if (cursor.moveToNext()) {
			result = getAccountingNotification(cursor);
		}

		cursor.close();
		return result;
	}

	/**
	 * 取得所有資料
	 *
	 * @return listAccountingNotification
	 */
	public List<AccountingNotification> getAll() {
		String orderBy = KEY_ID + " ASC";
		List<AccountingNotification> result = new ArrayList<>();
		Cursor cursor =
				database.query(ACCOUNTING_NOTIFICATION_TABLE_NAME, null, null, null, null, null,
						orderBy);

		while (cursor.moveToNext()) {
			result.add(getAccountingNotification(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * Cursor目前的資料包裝為物件
	 *
	 * @param cursor Cursor
	 * @return AccountingNotification
	 */
	private AccountingNotification getAccountingNotification(Cursor cursor) {
		// 準備回傳結果用的物件
		AccountingNotification accountingNotification = new AccountingNotification();

		accountingNotification.setId(cursor.getInt(0));
		accountingNotification.setNotificationName(cursor.getString(1));
		accountingNotification.setCategoryId(cursor.getInt(2));
		accountingNotification.setCategoryName(cursor.getString(3));
		accountingNotification.setCategoryIcon(cursor.getInt(4));
		accountingNotification.setNotificationHour(cursor.getInt(5));
		accountingNotification.setNotificationMinute(cursor.getInt(6));
		accountingNotification.setOn(cursor.getInt(7) == 1);

		// 回傳結果
		return accountingNotification;
	}

	/**
	 * 初始化帳戶資料
	 *
	 * @param database SQLiteDatabase
	 */
	public static void initData(Context context, SQLiteDatabase database) {
		database.execSQL(
				"insert into " + ACCOUNTING_NOTIFICATION_TABLE_NAME + "(" + NAME_COLUMN + "," +
						CATEGORY_ID_COLUMN + "," + CATEGORY_NAME_COLUMN + "," +
						CATEGORY_ICON_COLUMN + "," + NOTIFICATION_HOUR_COLUMN + "," +
						NOTIFICATION_MINUTE_COLUMN + "," + IS_ON_COLUMN + ") " +
						" values('午餐記帳通知','0','" + context.getString(
						ogl4jo3.shaowei.ogl4jo3.accounting.R.string.category_lunch) + "','" +
						R.drawable.ic_category_lunch + "','12'," + "'50','0')");

		database.execSQL(
				"insert into " + ACCOUNTING_NOTIFICATION_TABLE_NAME + "(" + NAME_COLUMN + "," +
						CATEGORY_ID_COLUMN + "," + CATEGORY_NAME_COLUMN + "," +
						CATEGORY_ICON_COLUMN + "," + NOTIFICATION_HOUR_COLUMN + "," +
						NOTIFICATION_MINUTE_COLUMN + "," + IS_ON_COLUMN + ") " +
						" values('晚餐記帳通知','1','" + context.getString(R.string.category_dinner) +
						"','" + R.drawable.ic_category_dinner + "','19'," + "'10','0')");
	}
}
