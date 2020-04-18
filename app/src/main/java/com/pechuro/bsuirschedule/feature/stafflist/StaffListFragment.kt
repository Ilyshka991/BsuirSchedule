package com.pechuro.bsuirschedule.feature.stafflist

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.ext.*
import kotlinx.android.synthetic.main.fragment_staff_list.*

class StaffListFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_staff_list

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(StaffListViewModel::class)
    }

    private val staffAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StaffAdapter(onItemClicked = {
            EventBus.send(StaffListItemSelectedEvent(it))
        }).also {
            it.setHasStableIds(true)
        }
    }
    private val args: StaffListFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onDetach() {
        super.onDetach()
        activity?.window?.clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onStart() {
        super.onStart()
        context?.showKeyboard(staffListNameInput)
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(staffListNameInput.windowToken)
    }

    override fun onDestroyView() {
        staffListRecyclerView.clearAdapter()
        super.onDestroyView()
    }

    private fun initView() {
        val inputType = when (args.staffType) {
            StaffType.GROUP -> InputType.TYPE_CLASS_NUMBER
            StaffType.EMPLOYEE -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        }
        staffListNameInput.inputType = inputType
        staffListNameInput.addTextListener {
            when (args.staffType) {
                StaffType.GROUP -> viewModel.filterGroups(it)
                StaffType.EMPLOYEE -> viewModel.filterEmployees(it)
            }
        }

        val hintRes = when (args.staffType) {
            StaffType.GROUP -> R.string.staff_list_hint_enter_group_number
            StaffType.EMPLOYEE -> R.string.staff_list_hint_enter_employee_name
        }
        staffListNameInputLayout.hint = getString(hintRes)

        staffListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = staffAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeData() {
        when (args.staffType) {
            StaffType.GROUP -> {
                viewModel.allGroupsData.nonNull().observe(viewLifecycleOwner) {
                    staffAdapter.submitList(it)
                }
            }
            StaffType.EMPLOYEE -> {
                viewModel.allEmployeesData.nonNull().observe(viewLifecycleOwner) {
                    staffAdapter.submitList(it)
                }
            }
        }
    }
}