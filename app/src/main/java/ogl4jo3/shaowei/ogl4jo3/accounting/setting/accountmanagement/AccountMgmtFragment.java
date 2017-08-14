package ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountMgmtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountMgmtFragment extends Fragment {

	//UI元件
	private Button btnNew;  //新增按鈕
	private RecyclerView mRecyclerView;    // RecyclerView
	private RecyclerView.LayoutManager mLayoutManager;
	private AccountAdapter mAdapter;

	private List<Account> accountList;

	public static final String ACCOUNT_MGMT_FRAGMENT_TAG = "ACCOUNT_MGMT_FRAGMENT_TAG";

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public AccountMgmtFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment AccountMgmtFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AccountMgmtFragment newInstance(String param1, String param2) {
		AccountMgmtFragment fragment = new AccountMgmtFragment();
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
		getActivity().setTitle(R.string.title_account_mgmt);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_account_mgmt, container, false);
		initAccountList();
		initUI(view);
		setRecyclerView();
		setOnClickListener();

		return view;
	}

	/**
	 * 初始化帳戶資料
	 */
	private void initAccountList() {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		accountList = new AccountDAO(db).getAll();

		/*Account account = new Account();
		account.setId(1);
		account.setName("測試用帳戶-卡片");
		account.setCategory(Account.CATEGORY_CARD);
		account.setBalance(1025);
		account.setDefaultAccount(false);
		account.setStartingAmount(50000);
		account.setBudgetPrice(10000);
		account.setBudgetNotice(20);
		Account account1 = new Account();
		account1.setId(2);
		account1.setName("測試用帳戶-銀行");
		account1.setCategory(Account.CATEGORY_BANK);
		account1.setBalance(999);
		account1.setDefaultAccount(false);
		account1.setStartingAmount(300);
		account1.setBudgetPrice(10000);
		account1.setBudgetNotice(20);
		accountList.add(account);
		accountList.add(account1);*/
	}

	/**
	 * 初始化UI元件
	 */
	private void initUI(View view) {
		btnNew = (Button) view.findViewById(R.id.btn_new);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_account);

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
		mAdapter = new AccountAdapter(getActivity(), getFragmentManager(), accountList);
		mRecyclerView.setAdapter(mAdapter);
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FragmentManager fragmentManager = getFragmentManager();
				AccountNewEditFragment accountNewEditFragment =
						AccountNewEditFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountNewEditFragment, null)
						.addToBackStack(null).commit();
			}
		});

	}

}
