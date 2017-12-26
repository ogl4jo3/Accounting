package ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import ogl4jo3.shaowei.ogl4jo3.accounting.common.income.IncomeDAO;

/**
 * 帳戶相關資料庫存取
 * Created by ogl4jo3 on 2017/8/10.
 */

public class AccountDAO {

	public static final String ALL_ACCOUNT = "所有帳戶";

	// 表格名稱
	private static final String ACCOUNT_TABLE_NAME = "account";

	// 編號表格欄位名稱，固定不變
	private static final String KEY_ID = "_id";
	// 表格欄位名稱
	private static final String NAME_COLUMN = "name";
	private static final String STARTING_AMOUNT_COLUMN = "startingAmount";
	private static final String ACCOUNT_CATEGORY_COLUMN = "accountCategory";
	private static final String DEFAULT_ACCOUNT_COLUMN = "defaultAccount";
	private static final String BUDGET_PRICE_COLUMN = "budgetPrice";
	private static final String BUDGET_NOTICE_COLUMN = "budgetNotice";

	private static final int DEFAULT_BUDGET_PRICE = 10000;//預設的預算
	private static final int DEFAULT_BUDGET_NOTICE = 20;//預設的預算提醒

	// CREATE_TABLE SQL指令
	public static final String CREATE_ACCOUNT_TABLE =
			"CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME + " (" + KEY_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COLUMN + " VARCHAR NOT NULL " +
					"UNIQUE," + STARTING_AMOUNT_COLUMN + " INTEGER," + ACCOUNT_CATEGORY_COLUMN +
					" INTEGER," + DEFAULT_ACCOUNT_COLUMN + " INTEGER," + BUDGET_PRICE_COLUMN +
					" INTEGER," + BUDGET_NOTICE_COLUMN + " INTEGER);";
	// DROP_TABLE SQL指令
	public static final String DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME;

	private final SQLiteDatabase database;

	/**
	 * 建構元
	 *
	 * @param database 資料庫
	 */
	public AccountDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * 新增帳戶
	 *
	 * @param account 支出
	 * @return 新增成功
	 */
	public boolean newAccount(Account account) {
		//帳戶名稱不可重複
		if (accountNameCount(account.getName()) > 0) {
			return false;
		}
		// 建立準備新增資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		if (account.isDefaultAccount()) {//若帳戶為預設，須將其他為預設的帳戶取消
			ContentValues cv = new ContentValues();
			cv.put(DEFAULT_ACCOUNT_COLUMN, 0);
			database.update(ACCOUNT_TABLE_NAME, cv, DEFAULT_ACCOUNT_COLUMN + "=?",
					new String[]{String.valueOf(1)});
		}

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, account.getName());
		contentValues.put(STARTING_AMOUNT_COLUMN, account.getStartingAmount());
		contentValues.put(ACCOUNT_CATEGORY_COLUMN, account.getCategory());
		contentValues.put(DEFAULT_ACCOUNT_COLUMN, account.isDefaultAccount() ? 1 : 0);
		contentValues.put(BUDGET_PRICE_COLUMN,
				account.getBudgetPrice() == null ? DEFAULT_BUDGET_PRICE : account.getBudgetPrice());
		contentValues.put(BUDGET_NOTICE_COLUMN,
				account.getBudgetNotice() == null ? DEFAULT_BUDGET_NOTICE :
						account.getBudgetNotice());

		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		return database.insert(ACCOUNT_TABLE_NAME, null, contentValues) > 0;
	}

	/**
	 * 儲存原有資料
	 */
	public boolean saveData(Account account) {
		//帳戶名稱不可重複
		if (accountNameCount(account.getName()) > 1) {
			return false;
		}
		// 建立準備修改資料的ContentValues物件
		ContentValues contentValues = new ContentValues();

		if (account.isDefaultAccount()) {//若帳戶為預設，須將其他為預設的帳戶取消
			ContentValues cv = new ContentValues();
			cv.put(DEFAULT_ACCOUNT_COLUMN, 0);
			database.update(ACCOUNT_TABLE_NAME, cv, DEFAULT_ACCOUNT_COLUMN + "=?",
					new String[]{String.valueOf(1)});
		}

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		contentValues.put(NAME_COLUMN, account.getName());
		contentValues.put(STARTING_AMOUNT_COLUMN, account.getStartingAmount());
		contentValues.put(ACCOUNT_CATEGORY_COLUMN, account.getCategory());
		contentValues.put(DEFAULT_ACCOUNT_COLUMN, account.isDefaultAccount() ? 1 : 0);
		contentValues.put(BUDGET_PRICE_COLUMN,
				account.getBudgetPrice() == null ? DEFAULT_BUDGET_PRICE : account.getBudgetPrice());
		contentValues.put(BUDGET_NOTICE_COLUMN,
				account.getBudgetNotice() == null ? DEFAULT_BUDGET_NOTICE :
						account.getBudgetNotice());

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(account.getId())};

		// 執行修改資料並回傳修改的資料數量是否成功
		return database.update(ACCOUNT_TABLE_NAME, contentValues, whereClause, whereArgs) > 0;
	}

	/**
	 * 刪除
	 */
	public boolean delAccount(Account account) {
		boolean isSuccess = false;
		if (getNumberOfAccounts() <= 1) {//若帳戶數量僅剩一個不可刪除
			return false;
		}
		if (account.isDefaultAccount()) {//若此帳戶為預設，須將預設帳戶轉為其他帳戶
			String selection = KEY_ID + "!=?";
			String[] selectionArgs = {String.valueOf(account.getId())};
			Account newDefaultAccount = new Account();
			Cursor cursor =
					database.query(ACCOUNT_TABLE_NAME, null, selection, selectionArgs, null, null,
							null);
			if (cursor.moveToNext()) {
				newDefaultAccount = getAccount(cursor);
			}
			cursor.close();

			ContentValues cv = new ContentValues();
			cv.put(DEFAULT_ACCOUNT_COLUMN, 1);
			database.update(ACCOUNT_TABLE_NAME, cv, KEY_ID + "=?",
					new String[]{String.valueOf(newDefaultAccount.getId())});
		}
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = {String.valueOf(account.getId())};
		if (database.delete(ACCOUNT_TABLE_NAME, whereClause, whereArgs) > 0) {
			//刪除帳戶時，同時刪除此帳戶所有紀錄(支出、收入)
			ExpensesDAO.delExpensesDataByAccount(database, account.getName());
			IncomeDAO.delIncomeDataByAccount(database, account.getName());
			isSuccess = true;
		}

		return isSuccess;
	}

	/**
	 * 檢查帳戶數量
	 */
	public int getNumberOfAccounts() {
		int result = 0;
		String queryString = "SELECT COUNT(" + KEY_ID + ") FROM " + ACCOUNT_TABLE_NAME;

		Cursor cursor = database.rawQuery(queryString, null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		//Log.d(TAG, "NumberOfAccounts: "+result);
		return result;
	}

	/**
	 * 取得所有帳戶預算總和
	 */
	public int getBudgetSumOfAccounts() {
		int result = 0;
		String queryString = "SELECT SUM(" + BUDGET_PRICE_COLUMN + ") FROM " + ACCOUNT_TABLE_NAME;

		Cursor cursor = database.rawQuery(queryString, null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		//Log.d(TAG, "NumberOfAccounts: "+result);
		return result;
	}

	/**
	 * 取得預設帳戶
	 *
	 * @return listAccount
	 */
	public Account getDefaultAccount() {
		Account result = new Account();
		String queryString =
				"SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + DEFAULT_ACCOUNT_COLUMN + "='1'";
		Cursor cursor = database.rawQuery(queryString, null);

		if (cursor.moveToNext()) {
			result = getAccount(cursor);
		}

		cursor.close();
		return result;
	}

	/**
	 * 依照ID取得帳戶
	 *
	 * @return listAccount
	 */
	public Account getAccountByName(String accountName) {
		Account result = new Account();

		String queryString = "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + NAME_COLUMN + "=?";
		Cursor cursor = database.rawQuery(queryString, new String[]{accountName});

		if (cursor.moveToNext()) {
			result = getAccount(cursor);
		}

		cursor.close();
		return result;
	}

	/**
	 * 取得所有資料
	 *
	 * @return listAccount
	 */
	public List<Account> getAll() {
		String orderBy = KEY_ID + " ASC";
		List<Account> result = new ArrayList<>();
		Cursor cursor = database.query(ACCOUNT_TABLE_NAME, null, null, null, null, null, orderBy);

		while (cursor.moveToNext()) {
			result.add(getAccount(cursor));
		}

		cursor.close();
		return result;
	}

	/**
	 * 檢查帳戶名稱是否已存在用
	 *
	 * @param accountName 帳戶名稱
	 * @return 此帳戶名稱數量
	 */
	public int accountNameCount(String accountName) {

		int count = 0;
		String queryString = "SELECT COUNT(" + KEY_ID + ") FROM " + ACCOUNT_TABLE_NAME + " WHERE " +
				NAME_COLUMN + "=?";
		String[] selectionArgs = new String[]{accountName};

		Cursor cursor = database.rawQuery(queryString, selectionArgs);

		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		//Log.d(TAG, "NumberOfAccounts: "+result);
		return count;
	}

	/**
	 * Cursor目前的資料包裝為物件
	 *
	 * @param cursor Cursor
	 * @return Account
	 */
	private Account getAccount(Cursor cursor) {
		// 準備回傳結果用的物件
		Account account = new Account();

		account.setId(cursor.getInt(0));
		account.setName(cursor.getString(1));
		account.setStartingAmount(cursor.getInt(2));
		account.setCategory(cursor.getInt(3));
		account.setDefaultAccount(cursor.getInt(4) == 1);
		account.setBudgetPrice(cursor.getInt(5));
		account.setBudgetNotice(cursor.getInt(6));

		//計算餘額: 餘額 = 起始金額 + 收入 - 支出
		int balance = account.getStartingAmount() +
				IncomeDAO.getSumByAccount(database, account.getName()) -
				ExpensesDAO.getSumByAccount(database, account.getName());
		account.setBalance(balance);

		// 回傳結果
		return account;
	}

	/**
	 * 初始化帳戶資料
	 *
	 * @param database SQLiteDatabase
	 */
	public static void initData(Context context, SQLiteDatabase database) {
		database.execSQL("insert into " + ACCOUNT_TABLE_NAME + "(" + NAME_COLUMN + "," +
				STARTING_AMOUNT_COLUMN + "," + ACCOUNT_CATEGORY_COLUMN + "," +
				DEFAULT_ACCOUNT_COLUMN + "," + BUDGET_PRICE_COLUMN + "," + BUDGET_NOTICE_COLUMN +
				") " + " values('帳戶名稱','0','0','1','5000','10')");
	}
}