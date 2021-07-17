package com.ogl4jo3.accounting.ui.categoryMgmt

import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.categorymanagement.Category
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO
import com.ogl4jo3.accounting.utils.database.MyDBHelper
import com.ogl4jo3.accounting.utils.keyboard.KeyboardUtil

//TODO: refactor IncomeCategoryMgmtFragment & ExpensesCategoryMgmtFragment by viewPager
class IncomeCategoryMgmtFragment : Fragment() {
    // UI元件
    private var btnNew: Button? = null
    private var mRecyclerView // RecyclerView
            : RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: CategoryAdapter? = null
    private var categoryList: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_mgmt_income, container, false)
        KeyboardUtil.closeKeyboard(activity)
        initCategoryList() //需放在onCreateView，從編輯頁跳回時才會重載資料
        initUI(view)
        setRecyclerView()
        setOnClickListener()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_hint, menu)
        //super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_hint) {
            Toast.makeText(activity, getString(R.string.hint_category_mgmt), Toast.LENGTH_SHORT)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 初始化資料
     */
    private fun initCategoryList() {
        val db = MyDBHelper.getDatabase(activity)
        categoryList = CategoryDAO(db).allIncomeCategories
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
    private fun initUI(view: View) {
        btnNew = view.findViewById<View>(R.id.btn_new) as Button
        mRecyclerView = view.findViewById<View>(R.id.rv_income_category) as RecyclerView
    }

    /**
     * 設置元件資料
     */
    private fun setViewData() {}

    /**
     * 設置RecyclerView
     */
    private fun setRecyclerView() {
        mRecyclerView!!.setHasFixedSize(true)
        // Layout管理器
        mLayoutManager = LinearLayoutManager(this.activity)
        mRecyclerView!!.layoutManager = mLayoutManager
        // Adapter
        mAdapter = IncomeCategoryAdapter(requireContext(), requireFragmentManager(), categoryList)
        mRecyclerView!!.adapter = mAdapter
        //設置拖拉移動item
        val mItemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun isLongPressDragEnabled(): Boolean {
                    //return super.isLongPressDragEnabled();
                    return true
                }

                override fun isItemViewSwipeEnabled(): Boolean {
                    return super.isItemViewSwipeEnabled()
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    //return false;
                    if (viewHolder.itemViewType != target.itemViewType) {
                        return false
                    }

                    // Notify the adapter of the move
                    (mAdapter as IncomeCategoryAdapter).onItemMove(
                        viewHolder.adapterPosition,
                        target.adapterPosition
                    )
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
                override fun onChildDraw(
                    c: Canvas, recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, dX: Float,
                    dY: Float, actionState: Int, isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive
                    )
                }
            })
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    /**
     * 設置OnClickListener
     */
    private fun setOnClickListener() {
        btnNew!!.setOnClickListener {
            findNavController().navigate(
                IncomeCategoryMgmtFragmentDirections.actionIncomeCategoryMgmtFragmentToIncomeCategoryNewEditFragment(
                    title = resources.getString(R.string.title_expense_category_add),
                    categoryJsonStr = null
                )
            )
        }
    }

}