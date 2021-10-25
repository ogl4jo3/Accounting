package com.ogl4jo3.accounting.ui.categoryMgmt

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.databinding.FragmentCategoryEditBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard
import com.ogl4jo3.accounting.ui.common.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExpenseCategoryEditFragment : Fragment() {

    private val binding by viewBinding(FragmentCategoryEditBinding::inflate)
    private val args by navArgs<ExpenseCategoryEditFragmentArgs>()
    private val viewModel by viewModel<CategoryEditViewModel> {
        parametersOf(
            CategoryIcon(
                args.category.iconResName.drawableId(activity, resources),
                args.category.iconResName
            ),
            args.category
        )
    }

    private val categoryIconList by lazy { R.array.category_icon.toCategoryIconList(resources) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.apply {
            viewModel = this@ExpenseCategoryEditFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            rvCategories.layoutManager = GridLayoutManager(context, 5)
            categoryIconList = this@ExpenseCategoryEditFragment.categoryIconList
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvCategories.adapter = CategoryIconAdapter { categoryIcon ->
                this@ExpenseCategoryEditFragment.viewModel.selectCategoryIcon(categoryIcon)
            }
            btnDel.setOnClickListener { showDelConfirmDialog() }
        }
        viewModel.apply {
            nameEmptyError = {
                Snackbar.make(
                    binding.root,
                    getString(R.string.hint_type_category_name),
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
                    ExpenseCategoryEditFragmentDirections
                        .actionExpensesCategoryEditFragmentToExpenseCategoryMgmtFragment()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
    }

    private fun showDelConfirmDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(getString(R.string.msg_category_del_confirm, viewModel.category.name))
            setMessage(R.string.msg_category_del_confirm_hint)
            setPositiveButton(R.string.btn_del) { _, _ ->
                viewModel.deleteCategory(
                    onSuccess = {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.msg_category_deleted, viewModel.category.name),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    },
                    onFail = {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.msg_at_least_one_category),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            setNegativeButton(R.string.btn_cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            create().show()
        }
    }
}