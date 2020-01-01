package com.pechuro.bsuirschedule.feature.main.employeedetails.employeeinfo

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.toDelete.entity.Employee
import com.pechuro.bsuirschedule.databinding.FragmentEmployeeDetailBinding
import com.pechuro.bsuirschedule.common.BaseFragment

class EmployeeDetailFragment : BaseFragment<FragmentEmployeeDetailBinding, EmployeeDetailFragmentViewModel>() {

    override val viewModel: EmployeeDetailFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(EmployeeDetailFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_employee_detail
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.data to employee)

    private val employee: Employee by lazy {
        arguments?.getParcelable<Employee>(ARG_EMPLOYEE) ?: throw IllegalArgumentException()
    }

    companion object {
        private const val ARG_EMPLOYEE = "arg_employee"

        fun newInstance(employee: Employee) = EmployeeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EMPLOYEE, employee)
            }
        }
    }
}