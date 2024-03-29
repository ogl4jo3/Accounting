package com.ogl4jo3.accounting.ui.categoryMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.databinding.FragmentCategoryMgmtBinding
import com.ogl4jo3.accounting.ui.BaseFragment
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class IncomeCategoryMgmtFragment : BaseFragment() {

    private val binding by viewBinding(FragmentCategoryMgmtBinding::inflate)
    private val viewModel by viewModel<CategoryMgmtViewModel> {
        parametersOf(CategoryType.Income)
    }

    private var categoryAdapter: CategoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@IncomeCategoryMgmtFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener {
                findNavController().navigate(
                    IncomeCategoryMgmtFragmentDirections
                        .actionIncomeCategoryMgmtFragmentToIncomeCategoryAddFragment()
                )
            }
            categoryAdapter =
                CategoryAdapter(this@IncomeCategoryMgmtFragment.viewModel)
            rvCategories.adapter = categoryAdapter
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun isLongPressDragEnabled(): Boolean {
                    //return true // TODO: workaround
                    return false // FIXME: fix and test feature of order number
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    // FIXME: fix and test feature of order number
                    return false // TODO: workaround
//                    if (viewHolder.itemViewType != target.itemViewType) {
//                        return false
//                    }
//
//                    // Notify the adapter of the move
//                    categoryAdapter?.onItemMove(
//                        viewHolder.absoluteAdapterPosition,
//                        target.absoluteAdapterPosition
//                    )
//                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            }).attachToRecyclerView(rvCategories)
        }
        viewModel.apply {
            navToItem = { category ->
                findNavController().navigate(
                    IncomeCategoryMgmtFragmentDirections
                        .actionIncomeCategoryMgmtFragmentToIncomeCategoryEditFragment(
                            category
                        )
                )
            }
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