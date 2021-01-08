package com.ogl4jo3.accounting.common.expenses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.setting.accountmanagement.Account;
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;
import com.ogl4jo3.accounting.utility.dialog.CategoryDialogFragment;
import com.ogl4jo3.accounting.utility.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesNewEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesNewEditFragment extends Fragment
		implements CategoryDialogFragment.onCategoryListener {

	//UI元件
	private TextView tvDate;    //日期
	private EditText etMoney;   //金額
	private LinearLayout llCategory;    //類別
	private ImageView ivCategoryIcon;   //類別圖示
	private TextView tvCategoryName;    //類別名稱
	private LinearLayout llAccount;    //帳戶
	private TextView tvAccountName;    //帳戶名稱
	private LinearLayout llFixedCharge;    //固定支出
	private EditText etDescription;   //描述
	private Button btnNew; //新增按鈕

	private int categoryId = -1;  //類別ID  未選擇時為-1
	private Account account;

	//編輯時用
	private Expenses expenses;
	private Button btnSave; //儲存按鈕
	private Button btnDel; //刪除按鈕

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String DATE_STR = "dateStr";
	private static final String EXPENSES_JSON = "expenses";

	private String dateStr;
	private String expensesJson;//如果expensesJson為空或null代表是新增，否則為編輯

	public ExpensesNewEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ExpensesNewEditFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ExpensesNewEditFragment newInstance(String param1, String param2) {
		ExpensesNewEditFragment fragment = new ExpensesNewEditFragment();
		Bundle args = new Bundle();
		args.putString(DATE_STR, param1);
		args.putString(EXPENSES_JSON, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			dateStr = getArguments().getString(DATE_STR);
			expensesJson = getArguments().getString(EXPENSES_JSON);
		}
		expenses = new Gson().fromJson(expensesJson, Expenses.class);
		SQLiteDatabase sqLiteDatabase = MyDBHelper.getDatabase(getActivity());
		account = new AccountDAO(sqLiteDatabase).getDefaultAccount();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		//新增、編輯標題
		getActivity().setTitle(
				StringUtil.isNullorEmpty(expensesJson) ? R.string.title_expenses_new :
						R.string.title_expenses_edit);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_expenses_new_edit, container, false);
		initUI(view);
		setViewData();
		setOnClickListener();
		return view;

	}

	/**
	 * 初始化元件
	 *
	 * @param view View
	 */
	private void initUI(View view) {
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		etMoney = (EditText) view.findViewById(R.id.et_money);
		llCategory = (LinearLayout) view.findViewById(R.id.ll_category);
		ivCategoryIcon = (ImageView) view.findViewById(R.id.iv_category_icon);
		tvCategoryName = (TextView) view.findViewById(R.id.tv_category_name);
		llAccount = (LinearLayout) view.findViewById(R.id.ll_account);
		tvAccountName = (TextView) view.findViewById(R.id.tv_account_name);
		llFixedCharge = (LinearLayout) view.findViewById(R.id.ll_fixed_charge);
		etDescription = (EditText) view.findViewById(R.id.et_description);
		btnNew = (Button) view.findViewById(R.id.btn_new);
		//編輯時用
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnDel = (Button) view.findViewById(R.id.btn_del);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		tvDate.setText(dateStr);
		/*if (!StringUtil.isNullorEmpty(expensesJson) && expenses.getId() == -1) {
			categoryId = expenses.getCategoryId();
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			Category category = new CategoryDAO(db).getExpensesData(categoryId);
			ivCategoryIcon.setImageResource(category.getIcon());
			tvCategoryName.setText(category.getName());
		} else if (!StringUtil.isNullorEmpty(expensesJson)) {//    編輯時
			btnNew.setVisibility(View.GONE);
			btnSave.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);

			etMoney.setText(String.valueOf(expenses.getPrice()));
			categoryId = expenses.getCategoryId();
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			Category category = new CategoryDAO(db).getExpensesData(categoryId);
			ivCategoryIcon.setImageResource(category.getIcon());
			tvCategoryName.setText(category.getName());
			account = new AccountDAO(db).getAccountByName(expenses.getAccountName());
			etDescription.setText(expenses.getDescription());
		}*/
		if (!StringUtil.isNullorEmpty(expensesJson)) {//    編輯和從通知進入時
			categoryId = expenses.getCategoryId();
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			Category category = new CategoryDAO(db).getExpensesData(categoryId);
			ivCategoryIcon.setImageResource(category.getIcon());
			tvCategoryName.setText(category.getName());
			etDescription.setText(expenses.getDescription());
			if(expenses.getId() != -1) {//    編輯時
				btnNew.setVisibility(View.GONE);
				btnSave.setVisibility(View.VISIBLE);
				btnDel.setVisibility(View.VISIBLE);

				etMoney.setText(String.valueOf(expenses.getPrice()));
				account = new AccountDAO(db).getAccountByName(expenses.getAccountName());
			}
		}
		tvAccountName.setText(account.getName());
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		llCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				CategoryDialogFragment categoryDialogFragment =
						CategoryDialogFragment.newInstance(CategoryDialogFragment.EXPENSES);
				categoryDialogFragment.setTargetFragment(ExpensesNewEditFragment.this, 0);
				categoryDialogFragment.show(getFragmentManager(), null);
			}
		});
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String priceStr = etMoney.getText().toString();
				String description = etDescription.getText().toString();
				if (priceStr.isEmpty() && categoryId == -1) {
					Toast.makeText(getActivity(), R.string.msg_input_money_category_account,
							Toast.LENGTH_SHORT).show();
					return;
				} else if (priceStr.isEmpty()) {
					Toast.makeText(getActivity(), R.string.msg_input_money, Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (categoryId == -1) {
					Toast.makeText(getActivity(), R.string.msg_input_category, Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (StringUtil.isNullorEmpty(account.getName())) {
					Toast.makeText(getActivity(), R.string.msg_input_account, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				int price = Integer.valueOf(priceStr);

				Expenses expenses = new Expenses();
				expenses.setPrice(price);
				expenses.setCategoryId(categoryId);
				expenses.setAccountName(account.getName());
				expenses.setDescription(description);
				expenses.setRecordTime(dateStr);
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new ExpensesDAO(db).newExpensesData(expenses);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		llAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				final List<Account> accountList = new AccountDAO(db).getAll();
				final List<String> accountsName = new ArrayList<>();
				for (Account account : accountList) {
					accountsName.add(account.getName());
				}

				new AlertDialog.Builder(getActivity()).setTitle(R.string.msg_choose_account)
						.setItems(accountsName.toArray(new String[accountsName.size()]),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int position) {
										String accountName = accountsName.get(position);
										tvAccountName.setText(accountName);
										account = accountList.get(position);

									}
								}).show();
			}
		});
		llFixedCharge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//TODO:
				Toast.makeText(getActivity(), getString(R.string.msg_todo), Toast.LENGTH_SHORT)
						.show();
			}
		});
		//編輯用
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String priceStr = etMoney.getText().toString();
				String description = etDescription.getText().toString();
				if (priceStr.isEmpty()) {
					Toast.makeText(getActivity(), R.string.msg_input_money, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				int price = Integer.valueOf(priceStr);

				expenses.setPrice(price);
				expenses.setCategoryId(categoryId);
				expenses.setAccountName(account.getName());
				expenses.setDescription(description);
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new ExpensesDAO(db).saveExpensesData(expenses);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		btnDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.msg_expenses_del_confirm);
				builder.setPositiveButton(R.string.btn_del, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
						new ExpensesDAO(db).delExpensesData(expenses.getId());

						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.popBackStack();
						dialogInterface.dismiss();
					}
				});
				builder.setNegativeButton(R.string.btn_cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.dismiss();
							}
						});
				builder.create().show();

			}
		});
	}

	@Override
	public void chooseCategory(int categoryID) {
		//Toast.makeText(getActivity(), "CategoryID:" + categoryID, Toast.LENGTH_SHORT).show();
		this.categoryId = categoryID;

		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		Category categoryTmp = new CategoryDAO(db).getExpensesData(categoryID);
		ivCategoryIcon.setImageResource(categoryTmp.getIcon());
		tvCategoryName.setText(categoryTmp.getName());
	}

}
