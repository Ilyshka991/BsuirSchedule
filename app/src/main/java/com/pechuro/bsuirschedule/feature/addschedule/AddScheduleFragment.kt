package com.pechuro.bsuirschedule.feature.addschedule

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleContainerPagerAdapter.FragmentType
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel.State
import com.pechuro.bsuirschedule.feature.stafflist.StaffAdapter
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.EmployeeInfo
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.GroupInfo
import com.pechuro.bsuirschedule.feature.stafflist.StaffListViewModel
import com.pechuro.bsuirschedule.feature.stafflist.StaffType
import kotlinx.android.synthetic.main.fragment_add_schedule.*

class AddScheduleFragment : BaseFragment() {

    companion object {

        private const val ARG_SCHEDULE_TYPE = "ARG_SCHEDULE_TYPE"

        fun newInstance(scheduleType: FragmentType) =
                AddScheduleFragment().apply {
                    arguments = bundleOf(ARG_SCHEDULE_TYPE to scheduleType)
                }
    }

    override val layoutId: Int = R.layout.fragment_add_schedule

    private val addScheduleViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val owner = parentFragment ?: this
        initViewModel(AddScheduleViewModel::class, owner = owner)
    }

    private val staffViewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(StaffListViewModel::class)
    }

    private val scheduleType: FragmentType by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getSerializable(ARG_SCHEDULE_TYPE) as FragmentType
    }

    private val staffAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StaffAdapter(onItemClicked = {
            addScheduleSuggestionsRecyclerView.tag = it
            loadSchedule(it)
        }).also {
            it.setHasStableIds(true)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val staffType = when (scheduleType) {
            FragmentType.EMPLOYEE -> StaffType.EMPLOYEE
            FragmentType.STUDENT -> StaffType.GROUP
        }
        staffViewModel.init(staffType)
        initView()
        observeData()
    }

    override fun onStart() {
        super.onStart()
        context?.showKeyboard(addScheduleNameInput)
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(addScheduleNameInput.windowToken)
    }

    override fun onDestroyView() {
        addScheduleSuggestionsRecyclerView.clearAdapter()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        activity?.window?.clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun initView() {
        addScheduleNameInput.apply {
            val inputType = when (scheduleType) {
                FragmentType.STUDENT -> InputType.TYPE_CLASS_NUMBER
                FragmentType.EMPLOYEE -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }
            this.inputType = inputType

            addTextListener {
                staffViewModel.filter(it)
            }
        }

        addScheduleSuggestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = staffAdapter
            setHasFixedSize(true)
        }

        addScheduleRetryButton.setSafeClickListener {
            val info = addScheduleSuggestionsRecyclerView.tag as? StaffItemInformation
            if (info != null) {
                loadSchedule(info)
            } else {
                addScheduleViewModel.cancel()
            }
        }

        addScheduleCancelButton.setSafeClickListener {
            addScheduleViewModel.cancel()
        }
    }

    private fun observeData() {
        addScheduleViewModel.state.nonNull().observe(viewLifecycleOwner) {
            updateLayoutState(it)
        }
        staffViewModel.listData.nonNull().observe(viewLifecycleOwner) {
            staffAdapter.submitList(it)
        }
    }

    private fun loadSchedule(info: StaffItemInformation) {
        val scheduleTypes = sequence {
            if (addScheduleChipClasses.isChecked) yield(ScheduleType.CLASSES)
            if (addScheduleChipExams.isChecked) yield(ScheduleType.EXAMS)
        }.toList()
        when (info) {
            is GroupInfo -> {
                addScheduleViewModel.loadSchedule(info.group, scheduleTypes)
            }
            is EmployeeInfo -> {
                addScheduleViewModel.loadSchedule(info.employee, scheduleTypes)
            }
        }
    }

    private fun updateLayoutState(state: State) {
        when (state) {
            is State.Idle -> {
                addScheduleSuggestionsRecyclerView.tag = null
                addScheduleProgressBar.setVisibleWithAlpha(false)
                addScheduleErrorParentView.setVisibleWithAlpha(false)
                addScheduleParamsParentView.setVisibleWithAlpha(true)
                        .doOnEnd {
                            context?.showKeyboard(addScheduleNameInput)
                        }
                addScheduleNameInput.requestFocus()
            }
            is State.Loading -> {
                context?.hideKeyboard(addScheduleNameInput.windowToken)
                addScheduleErrorParentView.setVisibleWithAlpha(false)
                addScheduleParamsParentView.setVisibleWithAlpha(false)
                        .doOnEnd {
                            addScheduleProgressBar.setVisibleWithAlpha(true)
                        }
            }
            is State.Error -> {
                addScheduleProgressBar.setVisibleWithAlpha(false)
                addScheduleErrorParentView.setVisibleWithAlpha(true)
                addScheduleParamsParentView.setVisibleWithAlpha(false)
            }
        }
    }
}