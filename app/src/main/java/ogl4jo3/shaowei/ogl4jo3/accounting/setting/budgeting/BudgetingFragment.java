package ogl4jo3.shaowei.ogl4jo3.accounting.setting.budgeting;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.Account;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetingFragment extends Fragment {

	public static final String BUDGETING_FRAGMENT_TAG = "BUDGETING_FRAGMENT_TAG";

	//UI元件
	private TextView tvTotalBudget;
	private NumberPicker numberPicker;
	private Button btnSetting;
	private RecyclerView mRecyclerView;    // RecyclerView
	private RecyclerView.LayoutManager mLayoutManager;
	private AccountBudgetingAdapter mAdapter;

	private List<Account> accountList;
	private int totalBudget;//所有帳戶的預算總額

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public BudgetingFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment BudgetingFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static BudgetingFragment newInstance(String param1, String param2) {
		BudgetingFragment fragment = new BudgetingFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setTitle(R.string.title_budgeting);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_budgeting, container, false);
		initAccountList();
		initUI(view);
		setRecyclerView();
		setOnClickListener();
		setNumberPicker();
		tvTotalBudget.setText(StringUtil.toMoneyStr(totalBudget));

		return view;
	}

	/**
	 * 初始化帳戶資料
	 */
	private void initAccountList() {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		accountList = new AccountDAO(db).getAll();
		totalBudget = new AccountDAO(db).getBudgetSumOfAccounts();

	}

	/**
	 * 初始化UI元件
	 */
	private void initUI(View view) {
		tvTotalBudget = (TextView) view.findViewById(R.id.tv_total_budget);
		btnSetting = (Button) view.findViewById(R.id.btn_setting);
		numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_account_budgeting);

	}

	/**
	 * 設置RecyclerView
	 */
	private void setRecyclerView() {
		mRecyclerView.setHasFixedSize(true);
		// Layout管理器
		mLayoutManager = new LinearLayoutManager(this.getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		// Adapter
		mAdapter = new AccountBudgetingAdapter(getActivity(), getFragmentManager(), accountList);
		mRecyclerView.setAdapter(mAdapter);
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		btnSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//小於10的話補0
				String budgetStartDay =
						numberPicker.getValue() < 10 ? "0" + numberPicker.getValue() :
								String.valueOf(numberPicker.getValue());
				//設定預算起始日
				new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
						.setString(SharedPreferencesTag.prefsBudgetStartDay, budgetStartDay);

				Toast.makeText(getActivity(), getString(R.string.msg_budgeting_start_day_changed,
						numberPicker.getValue()), Toast.LENGTH_SHORT).show();
				btnSetting.setClickable(false);
				btnSetting.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_dark));
			}
		});

	}

	/**
	 * 設置 NumberPicker
	 */
	private void setNumberPicker() {
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(31);
		//取得原先預算起始日
		String budgetStartDay =
				new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
						.getString(SharedPreferencesTag.prefsBudgetStartDay, "01");
		numberPicker.setValue(Integer.valueOf(budgetStartDay));
		numberPicker.setWrapSelectorWheel(false);
		numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
				btnSetting.setClickable(true);
				btnSetting.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
			}
		});
	}

}
