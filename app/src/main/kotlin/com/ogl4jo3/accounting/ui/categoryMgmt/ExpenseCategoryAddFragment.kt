package com.ogl4jo3.accounting.ui.categoryMgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentCategoryAddBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExpenseCategoryAddFragment : Fragment() {

    private val binding by viewBinding(FragmentCategoryAddBinding::inflate)
    private val viewModel by viewModel<ExpenseCategoryAddViewModel> {
        parametersOf(
            CategoryIcon(
                R.drawable.ic_category_other,
                resources.getResourceEntryName(R.drawable.ic_category_other)
            )
        )
    }

    private val categoryIconList = mutableListOf<CategoryIcon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCategoryIconRes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseCategoryAddFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            rvCategories.layoutManager = GridLayoutManager(context, 5)
            categoryIconList = this@ExpenseCategoryAddFragment.categoryIconList
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvCategories.adapter = CategoryIconAdapter { categoryIcon ->
                this@ExpenseCategoryAddFragment.viewModel.selectCategoryIcon(categoryIcon)
            }
        }
        viewModel.apply {
            nameEmptyError = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.hint_input_category_name),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            nameExistError = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.hint_duplicated_category),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            navPopBackStack = {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }

    private fun initCategoryIconRes() {
        val icons = resources.obtainTypedArray(R.array.category_icon)
        for (i in 0 until icons.length()) {
            val resId = icons.getResourceId(i, -1)
            val resEntryName = resources.getResourceEntryName(resId)
            categoryIconList.add(CategoryIcon(resId, resEntryName))
        }
        icons.recycle()
    }
}