package com.ogl4jo3.accounting.ui.categoryMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentCategoryMgmtExpenseBinding
import com.ogl4jo3.accounting.ui.common.viewBinding

class ExpenseCategoryMgmtFragment : Fragment() {

    private val binding by viewBinding(FragmentCategoryMgmtExpenseBinding::inflate)
    private val viewModel by viewModels<ExpenseCategoryMgmtViewModel>()

    private var expenseCategoryAdapter: ExpenseCategoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseCategoryMgmtFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener {
                findNavController().navigate(
                    ExpenseCategoryMgmtFragmentDirections.actionExpenseCategoryMgmtFragmentToExpensesCategoryAddFragment()
                )
            }
            expenseCategoryAdapter =
                ExpenseCategoryAdapter(this@ExpenseCategoryMgmtFragment.viewModel)
            rvExpenseCategories.adapter = expenseCategoryAdapter
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun isLongPressDragEnabled(): Boolean {
                    return true
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    if (viewHolder.itemViewType != target.itemViewType) {
                        return false
                    }

                    // Notify the adapter of the move
                    expenseCategoryAdapter?.onItemMove(
                        viewHolder.absoluteAdapterPosition,
                        target.absoluteAdapterPosition
                    )
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            }).attachToRecyclerView(rvExpenseCategories)
        }
        viewModel.apply {
            updateAllCategories()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_hint, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_hint) {
            Snackbar.make(
                binding.root,
                getString(R.string.hint_category_mgmt),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

}