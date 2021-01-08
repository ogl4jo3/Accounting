package com.ogl4jo3.accounting.setting.budgeting;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.ogl4jo3.accounting.setting.accountmanagement.Account;
import com.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;
import com.ogl4jo3.accounting.utils.keyboard.KeyboardUtil;
import com.ogl4jo3.accounting.utils.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetEditFragment extends Fragment {

	//UI元件
	private EditText etBudget;
	private SeekBar seekBarBudgetNotice;
	private TextView tvNoticeMsg;
	private Button btnSave;

	private Account account;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ACCOUNT_JSON = "accountJson";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String accountJson;
	private String mParam2;

	public BudgetEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment BudgetEditFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static BudgetEditFragment newInstance(String param1, String param2) {
		BudgetEditFragment fragment = new BudgetEditFragment();
		Bundle args = new Bundle();
		args.putString(ACCOUNT_JSON, param1);
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
		getActivity().setTitle(com.ogl4jo3.accounting.R.string.title_budget_edit);
		// Inflate the layout for this fragment
		View view = inflater.inflate(com.ogl4jo3.accounting.R.layout.fragment_budget_edit, container, false);
		initUI(view);
		setViewData();
		setOnClickListener();

		return view;
	}

	/**
	 * 初始化UI元件
	 */
	private void initUI(View view) {
		etBudget = (EditText) view.findViewById(com.ogl4jo3.accounting.R.id.et_budget);
		seekBarBudgetNotice = (SeekBar) view.findViewById(com.ogl4jo3.accounting.R.id.seekBarBudgetNotice);
		tvNoticeMsg = (TextView) view.findViewById(com.ogl4jo3.accounting.R.id.tv_budget_notice_msg);
		btnSave = (Button) view.findViewById(com.ogl4jo3.accounting.R.id.btn_save);

	}

	/**
	 * 設置資料
	 */
	private void setViewData() {
		etBudget.setText(String.valueOf(account.getBudgetPrice()));
		seekBarBudgetNotice.setProgress(account.getBudgetNotice());
		tvNoticeMsg.setText(
				getString(com.ogl4jo3.accounting.R.string.tv_budget_notice_msg, seekBarBudgetNotice.getProgress()));

	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		seekBarBudgetNotice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
				//以五為一個單位
				progress = progress / 5;
				progress = progress * 5;
				tvNoticeMsg.setText(getString(com.ogl4jo3.accounting.R.string.tv_budget_notice_msg, progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (StringUtil.isNullorEmpty(etBudget.getText().toString())) {
					Toast.makeText(getActivity(), "請輸入預算金額", Toast.LENGTH_SHORT).show();
					return;
				}
				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				account.setBudgetPrice(Integer.valueOf(etBudget.getText().toString()));
				//以五為一個單位
				int progress = seekBarBudgetNotice.getProgress();
				progress = progress / 5;
				progress = progress * 5;
				account.setBudgetNotice(progress);
				new AccountDAO(db).saveData(account);
				KeyboardUtil.closeKeyboard(getActivity());
				getFragmentManager().popBackStack();
			}
		});

	}

}
