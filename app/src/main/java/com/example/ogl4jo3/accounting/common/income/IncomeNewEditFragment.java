package com.example.ogl4jo3.accounting.common.income;

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

import com.example.ogl4jo3.accounting.R;
import com.example.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.example.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.example.ogl4jo3.utility.database.MyDBHelper;
import com.example.ogl4jo3.utility.dialog.CategoryDialogFragment;
import com.example.ogl4jo3.utility.string.StringUtil;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeNewEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeNewEditFragment extends Fragment
		implements CategoryDialogFragment.onCategoryListener {

	//UI元件
	private TextView tvDate;    //日期
	private EditText etMoney;   //金額
	private LinearLayout llCategory;    //類別
	private ImageView ivCategoryIcon;   //類別圖示
	private TextView tvCategoryName;    //類別名稱
	private LinearLayout llAccount;    //帳戶
	private LinearLayout llStableIncome;    //固定收入
	private EditText etDescription;   //描述
	private Button btnNew; //新增按鈕

	private int categoryId = -1;  //類別ID  未選擇時為-1

	//編輯時用
	private Income income;
	private Button btnSave; //儲存按鈕
	private Button btnDel; //刪除按鈕

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String DATE_STR = "dateStr";
	private static final String INCOME_JSON = "income";

	private String dateStr;
	private String incomeJson;//如果incomeJson為空或null代表是新增，否則為編輯

	public IncomeNewEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param dateStr    Parameter 1.
	 * @param incomeJson Parameter 2.
	 * @return A new instance of fragment IncomeNewEditFragment.
	 */
	public static IncomeNewEditFragment newInstance(String dateStr, String incomeJson) {
		IncomeNewEditFragment fragment = new IncomeNewEditFragment();
		Bundle args = new Bundle();
		args.putString(DATE_STR, dateStr);
		args.putString(INCOME_JSON, incomeJson);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			dateStr = getArguments().getString(DATE_STR);
			incomeJson = getArguments().getString(INCOME_JSON);
		}
		income = new Gson().fromJson(incomeJson, Income.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		//新增、編輯標題
		getActivity().setTitle(StringUtil.isNullorEmpty(incomeJson) ? R.string.title_income_new :
				R.string.title_income_edit);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_income_new_edit, container, false);
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
		llStableIncome = (LinearLayout) view.findViewById(R.id.ll_stable_income);
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
		if (!StringUtil.isNullorEmpty(incomeJson)) {//    編輯時
			btnNew.setVisibility(View.GONE);
			btnSave.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);

			etMoney.setText(String.valueOf(income.getPrice()));
			categoryId = income.getCategoryId();
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			Category category = new CategoryDAO(db).getIncomeData(categoryId);
			ivCategoryIcon.setImageResource(category.getIcon());
			tvCategoryName.setText(category.getName());
			etDescription.setText(income.getDescription());
		}
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		llCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				CategoryDialogFragment categoryDialogFragment =
						CategoryDialogFragment.newInstance(CategoryDialogFragment.INCOME);
				categoryDialogFragment.setTargetFragment(IncomeNewEditFragment.this, 0);
				categoryDialogFragment.show(getFragmentManager(), null);
			}
		});
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String priceStr = etMoney.getText().toString();
				String description = etDescription.getText().toString();
				if (priceStr.isEmpty() && categoryId == -1) {
					Toast.makeText(getActivity(), R.string.msg_input_money_category,
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
				}
				int price = Integer.valueOf(priceStr);

				Income income = new Income();
				income.setPrice(price);
				income.setCategoryId(categoryId);
				income.setDescription(description);
				income.setRecordTime(dateStr);
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new IncomeDAO(db).newIncomeData(income);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		llAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(getActivity(), "尚無此功能，待開發", Toast.LENGTH_SHORT).show();
			}
		});
		llStableIncome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(getActivity(), "尚無此功能，待開發", Toast.LENGTH_SHORT).show();
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

				income.setPrice(price);
				income.setCategoryId(categoryId);
				income.setDescription(description);
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new IncomeDAO(db).saveIncomeData(income);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		btnDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.msg_income_del_confirm);
				builder.setPositiveButton(R.string.btn_del, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
						new IncomeDAO(db).delIncomeData(income.getId());

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
		Category categoryTmp = new CategoryDAO(db).getIncomeData(categoryID);
		ivCategoryIcon.setImageResource(categoryTmp.getIcon());
		tvCategoryName.setText(categoryTmp.getName());
	}

}
