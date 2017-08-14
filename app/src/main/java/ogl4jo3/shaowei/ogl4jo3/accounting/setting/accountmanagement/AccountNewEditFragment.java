package ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountNewEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountNewEditFragment extends Fragment {

	//UI元件
	private EditText etAccountName;
	private EditText etStartingAmount;
	private ImageView ivAccountCategoryIcon;
	private Spinner spAccountCategory;
	private Switch swDefaultAccount;
	private Button btnNew; //新增按鈕

	//編輯時用
	private Account account;
	private Button btnSave; //儲存按鈕
	private Button btnDel; //刪除按鈕

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ACCOUNT_JSON = "account_json";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String accountJson;
	private String mParam2;

	public AccountNewEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param accountJson Parameter 1.
	 * @param param2      Parameter 2.
	 * @return A new instance of fragment AccountNewEditFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AccountNewEditFragment newInstance(String accountJson, String param2) {
		AccountNewEditFragment fragment = new AccountNewEditFragment();
		Bundle args = new Bundle();
		args.putString(ACCOUNT_JSON, accountJson);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			accountJson = getArguments().getString(ACCOUNT_JSON);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		account = new Gson().fromJson(accountJson, Account.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		//新增、編輯標題
		getActivity().setTitle(StringUtil.isNullorEmpty(accountJson) ? R.string.title_account_new :
				R.string.title_account_edit);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_account_new_edit, container, false);

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
		etAccountName = (EditText) view.findViewById(R.id.et_account_name);
		etStartingAmount = (EditText) view.findViewById(R.id.et_starting_amount);
		ivAccountCategoryIcon = (ImageView) view.findViewById(R.id.iv_account_category_icon);
		spAccountCategory = (Spinner) view.findViewById(R.id.sp_account_category);
		swDefaultAccount = (Switch) view.findViewById(R.id.sw_default_account);
		btnNew = (Button) view.findViewById(R.id.btn_new);
		//編輯時用
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnDel = (Button) view.findViewById(R.id.btn_del);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		if (!StringUtil.isNullorEmpty(accountJson)) {//    編輯時
			btnNew.setVisibility(View.GONE);
			btnSave.setVisibility(View.VISIBLE);
			SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
			if (new AccountDAO(db).getNumberOfAccounts() > 1) {//若帳戶數量大於一個才顯示刪除按鈕
				btnDel.setVisibility(View.VISIBLE);
			}

			etAccountName.setText(account.getName());
			etStartingAmount.setText(String.valueOf(account.getStartingAmount()));
			ivAccountCategoryIcon.setImageResource(account.getCategoryIcon());
			spAccountCategory.setSelection(account.getCategory());
			swDefaultAccount.setChecked(account.isDefaultAccount());
		}
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		spAccountCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position,
			                           long l) {
				int categoryIcon = 0;
				switch (position) {
					case Account.CATEGORY_CASH:
						categoryIcon = R.drawable.ic_account_category_cash;
						break;
					case Account.CATEGORY_CARD:
						categoryIcon = R.drawable.ic_account_category_card;
						break;
					case Account.CATEGORY_BANK:
						categoryIcon = R.drawable.ic_account_category_bank;
						break;
				}
				ivAccountCategoryIcon.setImageResource(categoryIcon);

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String accountName = etAccountName.getText().toString();
				if (StringUtil.isNullorEmpty(accountName)) {
					Toast.makeText(getActivity(), R.string.msg_input_account_name,
							Toast.LENGTH_SHORT).show();
					return;
				}
				Account account = new Account();
				account.setName(accountName);
				account.setStartingAmount(Integer.valueOf(etStartingAmount.getText().toString()));
				account.setCategory(spAccountCategory.getSelectedItemPosition());
				account.setDefaultAccount(swDefaultAccount.isChecked());

				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new AccountDAO(db).newAccount(account);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		//編輯用
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String accountName = etAccountName.getText().toString();
				if (StringUtil.isNullorEmpty(accountName)) {
					Toast.makeText(getActivity(), R.string.msg_input_account_name,
							Toast.LENGTH_SHORT).show();
					return;
				}
				Account accountNew = account;
				accountNew.setName(accountName);
				accountNew
						.setStartingAmount(Integer.valueOf(etStartingAmount.getText().toString()));
				accountNew.setCategory(spAccountCategory.getSelectedItemPosition());
				accountNew.setDefaultAccount(swDefaultAccount.isChecked());

				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				new AccountDAO(db).saveData(accountNew);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		});
		btnDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.msg_expenses_del_confirm);
				builder.setMessage(R.string.msg_account_del_confirm_hint);
				builder.setPositiveButton(R.string.btn_del, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
						new AccountDAO(db).delAccount(account);
						Toast.makeText(getActivity(), getString(R.string.msg_account_deleted, account.getName()),
								Toast.LENGTH_SHORT).show();

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

}
