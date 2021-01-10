package com.ogl4jo3.accounting.utils.dialog;

import androidx.fragment.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;

/**
 * 選擇類別對話框
 * Created by ogl4jo3 on 2017/7/23.
 */

public class CategoryDialogFragment extends DialogFragment {

	private onCategoryListener mListener;

	//UI元件
	private ListView lvCategory;

	public static final String EXPENSES = "expenses";
	public static final String INCOME = "income";
	private static final String EXPENSES_INCOME = "expenses_income";
	private String expenses_income;
	private SimpleAdapter categoryAdapter;
	private List<Map<String, Object>> mapData;  //SimpleAdapter 資料
	private final String mapNameKey = "name";   //adapter 資料對應KEY
	private final String mapIconKey = "icon";   //adapter 資料對應KEY
	private List<Category> categoryList;

	public interface onCategoryListener {

		public void chooseCategory(int categoryID);
	}

	public CategoryDialogFragment() {
		// Required empty public constructor
	}

	public static CategoryDialogFragment newInstance(String expenses_income) {

		CategoryDialogFragment fragment = new CategoryDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXPENSES_INCOME, expenses_income);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			expenses_income = getArguments().getString(EXPENSES_INCOME);
		}

		try {
			mListener = (onCategoryListener) getTargetFragment();
		} catch (ClassCastException e) {
			throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
		}

		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		//取得支出或收入的類別
		if (expenses_income.equalsIgnoreCase(EXPENSES)) {
			categoryList = new CategoryDAO(db).getAllExpensesCategories();
		} else if (expenses_income.equalsIgnoreCase(INCOME)) {
			categoryList = new CategoryDAO(db).getAllIncomeCategories();
		}

		int categoriesLen = categoryList.size();
		mapData = new ArrayList<>();
		for (int i = 0; i < categoriesLen; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put(mapNameKey, categoryList.get(i).getName());
			data.put(mapIconKey, categoryList.get(i).getIcon());
			mapData.add(data);
		}

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_category, container, false);
		getDialog().setTitle(R.string.tv_category);

		lvCategory = (ListView) view.findViewById(R.id.lv_category);
		categoryAdapter = new SimpleAdapter(getActivity(), mapData, R.layout.item_category_no_swipe,
				new String[]{mapNameKey, mapIconKey}, new int[]{R.id.tv_name, R.id.iv_icon});
		lvCategory.setAdapter(categoryAdapter);
		lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				//Log.d(TAG, "onItemClick: " + categoryList.get(i).getName() + " ID:" +
				//		categoryList.get(i).getId());
				mListener.chooseCategory(categoryList.get(i).getId());
				dismiss();
			}
		});

		return view;
	}
}
