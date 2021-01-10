package com.ogl4jo3.accounting.setting.accountingnotification;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;
import com.ogl4jo3.accounting.utils.dialog.CategoryDialogFragment;
import com.ogl4jo3.accounting.utils.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountingNotificationNewEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountingNotificationNewEditFragment extends Fragment
		implements CategoryDialogFragment.onCategoryListener {

	//UI元件
	private EditText etAccountingNotificationName;//記帳通知名稱
	private LinearLayout llCategory;    //類別
	private ImageView ivCategoryIcon;   //類別圖示
	private TextView tvCategoryName;    //類別名稱
	private NumberPicker numberPickerHour; //通知時間，時
	private NumberPicker numberPickerMinute; //通知時間，分
	private Button btnNew; //新增按鈕

	//編輯時用
	private AccountingNotification accountingNotification;
	private Button btnSave; //儲存按鈕
	private Button btnDel; //刪除按鈕

	private int categoryId = -1;  //類別ID  未選擇時為-1

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ACCOUNTING_NOTIFICATION_ID_JSON = "accountingNotificationIdJson";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String accountingNotificationIdJson;
	private String mParam2;

	public AccountingNotificationNewEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param accountingNotificationJson Parameter 1.
	 * @param param2                     Parameter 2.
	 * @return A new instance of fragment AccountingNotificationNewEditFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AccountingNotificationNewEditFragment newInstance(
			String accountingNotificationJson, String param2) {
		AccountingNotificationNewEditFragment fragment =
				new AccountingNotificationNewEditFragment();
		Bundle args = new Bundle();
		args.putString(ACCOUNTING_NOTIFICATION_ID_JSON, accountingNotificationJson);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			accountingNotificationIdJson =
					getArguments().getString(ACCOUNTING_NOTIFICATION_ID_JSON);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		if (!StringUtil.isNullorEmpty(accountingNotificationIdJson)) {
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			accountingNotification = new AccountingNotificationDAO(db)
					.getById(Integer.valueOf(accountingNotificationIdJson));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		//新增、編輯標題
		getActivity().setTitle(StringUtil.isNullorEmpty(accountingNotificationIdJson) ?
				R.string.title_accounting_notification_new :
				R.string.title_accounting_notification_edit);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_accounting_notification_new_edit, container,
				false);

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
		etAccountingNotificationName =
				(EditText) view.findViewById(R.id.et_accounting_notification_name);
		llCategory = (LinearLayout) view.findViewById(R.id.ll_category);
		ivCategoryIcon = (ImageView) view.findViewById(R.id.iv_category_icon);
		tvCategoryName = (TextView) view.findViewById(R.id.tv_category_name);
		numberPickerHour = (NumberPicker) view.findViewById(R.id.number_picker_hour);
		numberPickerMinute = (NumberPicker) view.findViewById(R.id.number_picker_minute);
		btnNew = (Button) view.findViewById(R.id.btn_new);
		//編輯時用
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnDel = (Button) view.findViewById(R.id.btn_del);

		numberPickerHour.setMinValue(0);
		numberPickerHour.setMaxValue(23);
		numberPickerMinute.setMinValue(0);
		numberPickerMinute.setMaxValue(59);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		if (!StringUtil.isNullorEmpty(accountingNotificationIdJson)) {//    編輯時
			btnNew.setVisibility(View.GONE);
			btnSave.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			categoryId = accountingNotification.getCategoryId();
			etAccountingNotificationName.setText(accountingNotification.getNotificationName());
			ivCategoryIcon.setImageResource(accountingNotification.getCategoryIcon());
			tvCategoryName.setText(accountingNotification.getCategoryName());
			numberPickerHour.setValue(accountingNotification.getNotificationHour());
			numberPickerMinute.setValue(accountingNotification.getNotificationMinute());
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
						CategoryDialogFragment.newInstance(CategoryDialogFragment.EXPENSES);
				categoryDialogFragment
						.setTargetFragment(AccountingNotificationNewEditFragment.this, 0);
				categoryDialogFragment.show(getFragmentManager(), null);
			}
		});
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				String accountNotificationName = etAccountingNotificationName.getText().toString();
				if (StringUtil.isNullorEmpty(accountNotificationName)) {
					Toast.makeText(getActivity(), R.string.msg_input_name, Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (categoryId == -1) {
					Toast.makeText(getActivity(), R.string.msg_input_category, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Category category = new CategoryDAO(db).getExpensesData(categoryId);

				AccountingNotification accountingNotification = new AccountingNotification();
				accountingNotification.setNotificationName(accountNotificationName);
				accountingNotification.setCategoryId(categoryId);
				accountingNotification.setCategoryIcon(category.getIcon());
				accountingNotification.setCategoryName(category.getName());
				accountingNotification.setNotificationHour(numberPickerHour.getValue());
				accountingNotification.setNotificationMinute(numberPickerMinute.getValue());
				accountingNotification.setOn(false);

				new AccountingNotificationDAO(db).newAccountingNotification(accountingNotification);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		//編輯用
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				String accountNotificationName = etAccountingNotificationName.getText().toString();
				if (StringUtil.isNullorEmpty(accountNotificationName)) {
					Toast.makeText(getActivity(), R.string.msg_input_name, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Category category = new CategoryDAO(db).getExpensesData(categoryId);

				AccountingNotification accountingNotificationNew = accountingNotification;
				accountingNotificationNew.setNotificationName(accountNotificationName);
				accountingNotificationNew.setCategoryId(categoryId);
				accountingNotificationNew.setCategoryIcon(category.getIcon());
				accountingNotificationNew.setCategoryName(category.getName());
				accountingNotificationNew.setNotificationHour(numberPickerHour.getValue());
				accountingNotificationNew.setNotificationMinute(numberPickerMinute.getValue());
				accountingNotificationNew.setOn(false);

				new AccountingNotificationDAO(db).saveData(accountingNotification);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		btnDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.msg_notification_del_confirm);
				builder.setMessage(getString(R.string.msg_notification_del_confirm_hint,
						accountingNotification.getNotificationName()));
				builder.setPositiveButton(R.string.btn_del, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
						new AccountingNotificationDAO(db)
								.delAccountNotification(accountingNotification);
						Toast.makeText(getActivity(), getString(R.string.msg_notification_deleted,
								accountingNotification.getNotificationName()), Toast.LENGTH_SHORT)
								.show();

						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.popBackStack();
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
