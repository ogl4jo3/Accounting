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
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.databinding.FragmentCategoryAddBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class IncomeCategoryAddFragment : Fragment() {

    private val binding by viewBinding(FragmentCategoryAddBinding::inflate)
    private val viewModel by viewModel<CategoryAddViewModel> {
        parametersOf(
            CategoryType.Income,
            CategoryIcon(
                R.drawable.ic_category_other,
                R.drawable.ic_category_other.drawableName(resources)
            )
        )
    }

    private val categoryIconList by lazy { R.array.category_icon.toCategoryIconList(resources) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@IncomeCategoryAddFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            rvCategories.layoutManager = GridLayoutManager(context, 5)
            categoryIconList = this@IncomeCategoryAddFragment.categoryIconList
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvCategories.adapter = CategoryIconAdapter { categoryIcon ->
                this@IncomeCategoryAddFragment.viewModel.selectCategoryIcon(categoryIcon)
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
            navToCategoryMgmtFragment = {
                findNavController().navigate(
                    IncomeCategoryAddFragmentDirections
                        .actionIncomeCategoryAddFragmentToIncomeCategoryMgmtFragment()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }

}