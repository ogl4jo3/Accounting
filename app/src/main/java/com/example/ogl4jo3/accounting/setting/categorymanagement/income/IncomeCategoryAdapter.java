package com.example.ogl4jo3.accounting.setting.categorymanagement.income;

import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.ogl4jo3.accounting.R;
import com.example.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.example.ogl4jo3.accounting.setting.categorymanagement.CategoryAdapter;
import com.example.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.example.ogl4jo3.utility.database.MyDBHelper;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 收入類別itemAdapter
 * Created by ogl4jo3 on 2017/7/14.
 */

public class IncomeCategoryAdapter extends CategoryAdapter {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Category> categoryList;

	public IncomeCategoryAdapter(Context context, FragmentManager fragmentManager,
	                             List<Category> categoryList) {
		super(context, fragmentManager, categoryList);
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.categoryList = categoryList;
	}

	@Override
	public void onItemClick(int position) {
		String categoryJson = (new Gson()).toJson(categoryList.get(position));
		IncomeCategoryNewEditFragment incomeCategoryNewEditFragment =
				IncomeCategoryNewEditFragment.newInstance("edit", categoryJson);
		fragmentManager.beginTransaction()
				.replace(R.id.layout_main_content, incomeCategoryNewEditFragment, null)
				.addToBackStack(null).commit();

		//Toast.makeText(mContext, "Click on " + categoryList.get(position).getName(),
		//		Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemMove(int fromPosition, int toPosition) {
		//更新排列順序
		SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
		new CategoryDAO(db)
				.updateIncomeOrderNum(categoryList.get(fromPosition), categoryList.get(toPosition));

		//即時更新資料
		int fromOrderNum = categoryList.get(fromPosition).getOrderNum();
		int toOrderNum = categoryList.get(toPosition).getOrderNum();
		categoryList.get(fromPosition).setOrderNum(toOrderNum);
		categoryList.get(toPosition).setOrderNum(fromOrderNum);

		Log.d(TAG, "onItemMove: from:" + fromPosition + ", to:" + toPosition);

		Collections.swap(categoryList, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);

	}

	@Override
	public void onItemDismiss(int position) {
		//刪除
		SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
		new CategoryDAO(db).deleteIncomeData(categoryList.get(position));

		Toast.makeText(mContext, mContext.getResources()
						.getString(R.string.tv_category_deleted, categoryList.get(position).getName()),
				Toast.LENGTH_SHORT).show();
		categoryList.remove(position);
		notifyItemRemoved(position);

	}
}
