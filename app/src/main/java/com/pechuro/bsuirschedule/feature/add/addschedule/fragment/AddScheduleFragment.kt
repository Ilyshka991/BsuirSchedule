package com.pechuro.bsuirschedule.feature.add.addschedule.fragment

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleContainerPagerAdapter.FragmentType
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleViewModel.State
import com.pechuro.bsuirschedule.feature.add.addschedule.fragment.SuggestionItemInformation.EmployeeInfo
import com.pechuro.bsuirschedule.feature.add.addschedule.fragment.SuggestionItemInformation.GroupInfo
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

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val owner = parentFragment ?: this
        initViewModel(AddScheduleViewModel::class, owner = owner)
    }

    private val scheduleType: FragmentType by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getSerializable(ARG_SCHEDULE_TYPE) as FragmentType
    }

    private val suggestionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddScheduleSuggestionsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        addScheduleNameInput.apply {
            val inputType = when (scheduleType) {
                FragmentType.STUDENT -> InputType.TYPE_CLASS_NUMBER
                FragmentType.EMPLOYEE -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }
            this.inputType = inputType

            addTextListener {
                when (scheduleType) {
                    FragmentType.STUDENT -> viewModel.filterGroups(it)
                    FragmentType.EMPLOYEE -> viewModel.filterEmployees(it)
                }
            }
        }

        addScheduleSuggestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = suggestionsAdapter.apply {
                onItemClicked = {
                    addScheduleSuggestionsRecyclerView.tag = it
                    loadSchedule(it)
                }
            }
            itemAnimator = null
        }

        addScheduleRetryButton.setOnClickListener {
            val info = addScheduleSuggestionsRecyclerView.tag as? SuggestionItemInformation
            if (info != null) {
                loadSchedule(info)
            } else {
                viewModel.cancel()
            }
        }

        addScheduleCancelButton.setOnClickListener {
            viewModel.cancel()
        }
    }

    private fun observeData() {
        viewModel.state.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is State.Idle -> {
                    addScheduleSuggestionsRecyclerView.tag = null
                    addScheduleProgressBar.isVisible = false
                    addScheduleErrorParentView.isVisible = false
                    addScheduleParamsParentView.isVisibleOrInvisible = true
                    addScheduleNameInput.requestFocus()
                    context?.showKeyboard(addScheduleNameInput)
                }
                is State.Loading -> {
                    addScheduleProgressBar.isVisible = true
                    addScheduleErrorParentView.isVisible = false
                    addScheduleParamsParentView.isVisibleOrInvisible = false
                    context?.hideKeyboard(addScheduleNameInput.windowToken)
                }
                is State.Error -> {
                    addScheduleProgressBar.isVisible = false
                    addScheduleErrorParentView.isVisible = true
                    addScheduleParamsParentView.isVisibleOrInvisible = false
                }
            }
        }

        when (scheduleType) {
            FragmentType.STUDENT -> {
                viewModel.allGroupsData.observeNonNull(viewLifecycleOwner) {
                    suggestionsAdapter.submitList(it)
                }
            }
            FragmentType.EMPLOYEE -> {
                viewModel.allEmployeesData.observeNonNull(viewLifecycleOwner) {
                    suggestionsAdapter.submitList(it)
                }
            }
        }
    }

    private fun loadSchedule(info: SuggestionItemInformation) {
        val scheduleTypes = sequence {
            if (addScheduleChipClasses.isChecked) yield(ScheduleType.CLASSES)
            if (addScheduleChipExams.isChecked) yield(ScheduleType.EXAMS)
        }.toList()
        when (info) {
            is GroupInfo -> {
                viewModel.loadSchedule(info.group, scheduleTypes)
            }
            is EmployeeInfo -> {
                viewModel.loadSchedule(info.employee, scheduleTypes)
            }
        }
    }
}