package ogl4jo3.shaowei.ogl4jo3.accounting.setting.categorymanagement.expenses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import ogl4jo3.shaowei.ogl4jo3.accounting.setting.categorymanagement.Category;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesCategoryNewEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesCategoryNewEditFragment extends Fragment {

	// UI元件
	private ImageView ivCategoryIcon;
	private EditText etCategoryName;
	private GridView gridViewIcon;
	private Button btnSave;
	private Button btnDel;

	private FragmentManager fragmentManager;
	private Category category;  //所選擇的類別
	private int[] iconArray;    //所有類別 Icon
	private int iconAmount;     //Icon數量
	private List<Map<String, Object>> mapData;  //GridView 顯示資料
	private final String mapIconKey = "icon";   //GridView 資料對應KEY
	private SimpleAdapter gridViewAdapter;
	private int categoryIcon;
	private int categoryID;

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String NEW_EDIT = "newedit";
	private static final String CATEGORY_JSON = "category";

	private String newEdit;
	private String categoryJson;

	public ExpensesCategoryNewEditFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param newEdit Parameter 1.
	 * @param categoryJson Parameter 2.
	 * @return A new instance of fragment ExpensesCategoryNewEditFragment.
	 */
	public static ExpensesCategoryNewEditFragment newInstance(String newEdit, String categoryJson) {
		ExpensesCategoryNewEditFragment fragment = new ExpensesCategoryNewEditFragment();
		Bundle args = new Bundle();
		args.putString(NEW_EDIT, newEdit);
		args.putString(CATEGORY_JSON, categoryJson);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			newEdit = getArguments().getString(NEW_EDIT);
			categoryJson = getArguments().getString(CATEGORY_JSON);
		}
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(ogl4jo3.shaowei.ogl4jo3.accounting.R.layout.fragment_category_new_edit, container, false);
		initUI(view);
		fragmentManager = getFragmentManager();
		setGridViewIcon();  //設置GridView相關

		if (newEdit.equalsIgnoreCase("new")) {  //新增
			getActivity().setTitle(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.title_expenses_category_new);
			ivCategoryIcon.setImageResource(ogl4jo3.shaowei.ogl4jo3.accounting.R.drawable.ic_category_other);
			categoryIcon = ogl4jo3.shaowei.ogl4jo3.accounting.R.drawable.ic_category_other;
			categoryID = -1;
			btnDel.setVisibility(View.GONE);    //新增時隱藏刪除按鈕
		} else {     //編輯
			getActivity().setTitle(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.title_expenses_category_edit);
			ivCategoryIcon.setImageResource(category.getIcon());
			categoryIcon = category.getIcon();
			categoryID = category.getId();
			etCategoryName.setText(category.getName());
		}
		setOnClickListener();

		return view;
	}

	/**
	 * 初始化資料
	 */
	private void initData() {
		category = new Gson().fromJson(categoryJson, Category.class);
		setIconArray(); //將IconArray從Resources取出並設置
		setGridViewData();  //設置GridViewAdapter所需資料
	}

	/**
	 * 初始化元件
	 *
	 * @param view View
	 */
	private void initUI(View view) {
		ivCategoryIcon = (ImageView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.iv_icon);
		etCategoryName = (EditText) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.et_name);
		btnSave = (Button) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.btn_new);
		btnDel = (Button) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.btn_del);
		gridViewIcon = (GridView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.gv_category_icon);
	}

	/**
	 * 設置GridView
	 */
	private void setGridViewIcon() {
		gridViewAdapter =
				new SimpleAdapter(this.getActivity(), mapData, ogl4jo3.shaowei.ogl4jo3.accounting.R.layout.item_category_icon,
						new String[]{mapIconKey}, new int[]{ogl4jo3.shaowei.ogl4jo3.accounting.R.id.iv_icon});
		//gridViewIcon.setNumColumns(5);
		gridViewIcon.setAdapter(gridViewAdapter);
		gridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ivCategoryIcon.setImageResource(iconArray[position]);
				categoryIcon = iconArray[position];
				//Toast.makeText(getActivity(), "你選擇了" + position, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 設置GridViewAdapter所需資料
	 */
	private void setGridViewData() {
		mapData = new ArrayList<>();
		for (int i = 0; i < iconAmount; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put(mapIconKey, iconArray[i]);
			mapData.add(data);
		}
	}

	/**
	 * 將IconArray從Resources取出並設置
	 */
	private void setIconArray() {
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(ogl4jo3.shaowei.ogl4jo3.accounting.R.array.category_icon);
		iconAmount = icons.length();
		iconArray = new int[iconAmount];
		for (int i = 0; i < iconAmount; i++) {
			iconArray[i] = icons.getResourceId(i, -1);
			//Log.d(TAG, "iconArray[i] " + iconArray[i]);
		}
		// recycle the array
		icons.recycle();
	}

	/**
	 * 儲存、刪除
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
				String categoryName = etCategoryName.getText().toString();
				if (categoryName.isEmpty()) {
					Toast.makeText(getActivity(), getString(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.hint_input_category_name),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (new CategoryDAO(db)
						.checkExpensesRepeated(categoryName, categoryIcon, categoryID)) {//檢查是否重複
					Toast.makeText(getActivity(), getString(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.hint_repeated_category),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Category categoryNew = new Category();
				categoryNew.setIcon(categoryIcon);
				categoryNew.setName(categoryName);

				if (newEdit.equalsIgnoreCase("new")) {
					new CategoryDAO(db).newExpensesData(categoryNew);
				} else {
					categoryNew.setId(category.getId());
					categoryNew.setOrderNum(category.getOrderNum());
					new CategoryDAO(db).saveExpensesData(categoryNew);
				}
				fragmentManager.popBackStack();

			}
		});
		btnDel.setOnClickListener(new View.OnClickListener()

		{

			@Override
			public void onClick(View view) {
				//刪除
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_category_del_confirm);
				builder.setMessage(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_category_del_confirm_hint);
				builder.setPositiveButton(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.btn_del, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
						new CategoryDAO(db).deleteExpensesData(category);
						Toast.makeText(getActivity(), getString(
								ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_category_deleted, category.getName()),
								Toast.LENGTH_SHORT).show();
						fragmentManager.popBackStack();
					}
				});
				builder.setNegativeButton(ogl4jo3.shaowei.ogl4jo3.accounting.R.string.btn_cancel,
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
