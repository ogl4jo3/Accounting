package com.ogl4jo3.accounting.setting.categorymanagement.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryAdapter;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;
import com.ogl4jo3.accounting.utility.keyboard.KeyboardUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesCategoryMgmtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesCategoryMgmtFragment extends Fragment {

	public static final String EXPENSES_CATEGORY_MGMT_FRAGMENT_TAG =
			"EXPENSES_CATEGORY_MGMT_FRAGMENT_TAG";

	// UI元件
	private Button btnNew;
	private RecyclerView mRecyclerView;    // RecyclerView
	private RecyclerView.LayoutManager mLayoutManager;
	private CategoryAdapter mAdapter;

	private List<Category> categoryList;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ExpensesCategoryMgmtFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ExpensesCategoryMgmtFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ExpensesCategoryMgmtFragment newInstance(String param1, String param2) {
		ExpensesCategoryMgmtFragment fragment = new ExpensesCategoryMgmtFragment();
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
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setTitle(R.string.title_expenses_category_mgmt);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_category_mgmt_expenses, container, false);
		KeyboardUtil.closeKeyboard(getActivity());
		initCategoryList(); //需放在onCreateView，從編輯頁跳回時才會重載資料
		initUI(view);
		setRecyclerView();
		setOnClickListener();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_category_mgmt, menu);
		//super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_hint) {
			Toast.makeText(getActivity(), getString(R.string.hint_category_mgmt),
					Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化資料
	 */
	private void initCategoryList() {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		categoryList = new CategoryDAO(db).getAllExpensesCategories();
		/*categoryList = new ArrayList<>();
		Category category = new Category();
		category.setName("其他");
		category.setIcon(R.drawable.ic_category_other);

		Category category1 = new Category();
		category1.setName("午餐");
		category1.setIcon(R.drawable.ic_category_lunch);
		Category category2 = new Category();
		category2.setName("晚餐");
		category2.setIcon(R.drawable.ic_category_dinner);
		Category category3 = new Category();
		category3.setName("下午茶");
		category3.setIcon(R.drawable.ic_category_afternoon_tea);
		categoryList.add(category);
		categoryList.add(category1);
		categoryList.add(category2);
		categoryList.add(category3);*/
	}

	/**
	 * 初始化元件
	 *
	 * @param view View
	 */
	private void initUI(View view) {
		btnNew = (Button) view.findViewById(R.id.btn_new);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_expenses_category);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
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
		mAdapter = new ExpensesCategoryAdapter(getActivity(), getFragmentManager(), categoryList);
		mRecyclerView.setAdapter(mAdapter);
		//設置拖拉移動item
		ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(
				new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

					@Override
					public boolean isLongPressDragEnabled() {
						//return super.isLongPressDragEnabled();
						return true;
					}

					@Override
					public boolean isItemViewSwipeEnabled() {
						return super.isItemViewSwipeEnabled();
					}

					@Override
					public boolean onMove(RecyclerView recyclerView,
					                      RecyclerView.ViewHolder viewHolder,
					                      RecyclerView.ViewHolder target) {
						//return false;
						if (viewHolder.getItemViewType() != target.getItemViewType()) {
							return false;
						}

						// Notify the adapter of the move
						mAdapter.onItemMove(viewHolder.getAdapterPosition(),
								target.getAdapterPosition());
						return true;
					}

					@Override
					public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

					}

					@Override
					public void onChildDraw(Canvas c, RecyclerView recyclerView,
					                        final RecyclerView.ViewHolder viewHolder, float dX,
					                        float dY, int actionState, boolean isCurrentlyActive) {
						super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
								isCurrentlyActive);

					}
				});

		mItemTouchHelper.attachToRecyclerView(mRecyclerView);
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FragmentManager fragmentManager = getFragmentManager();
				ExpensesCategoryNewEditFragment expensesCategoryNewEditFragment =
						ExpensesCategoryNewEditFragment.newInstance("new", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, expensesCategoryNewEditFragment, null)
						.addToBackStack(null).commit();
			}
		});
	}

}
