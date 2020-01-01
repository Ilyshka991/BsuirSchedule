package com.pechuro.bsuirschedule.feature.main.employeedetails

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.toDelete.entity.Employee
import com.pechuro.bsuirschedule.databinding.DialogEmployeeDetailsBinding
import com.pechuro.bsuirschedule.common.BaseDialog
import java.util.*
import javax.inject.Inject

class EmployeeDetailsDialog : BaseDialog<DialogEmployeeDetailsBinding, EmployeeDetailsDialogViewModel>() {

    @Inject
    lateinit var pagerAdapter: EmployeeDetailsPagerAdapter

    override val viewModel: EmployeeDetailsDialogViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(EmployeeDetailsDialogViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.dialog_employee_details
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val employees = arguments?.getParcelableArrayList<Employee>(ARG_EMPLOYEE_LIST)

        employees?.let {
            pagerAdapter.fragmentsInfo = it

            with(viewDataBinding.tabLayout) {
                if (it.size < 2) {
                    visibility = View.GONE
                } else {
                    repeat(it.size) { addTab(newTab()) }
                }
            }
        }

        viewDataBinding.viewPager.apply {
            adapter = pagerAdapter
            addOnPageChangeListener(
                    TabLayout.TabLayoutOnPageChangeListener(
                            viewDataBinding.tabLayout))
        }
    }

    companion object {
        const val TAG = "dialog_employee_details"
        private const val ARG_EMPLOYEE_LIST = "arg_employees"

        fun newInstance(employees: ArrayList<Employee>) = EmployeeDetailsDialog().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_EMPLOYEE_LIST, employees)
            }
        }
    }
}