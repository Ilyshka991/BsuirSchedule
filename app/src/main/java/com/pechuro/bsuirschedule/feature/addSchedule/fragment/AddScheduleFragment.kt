package com.pechuro.bsuirschedule.feature.addSchedule.fragment

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
import com.pechuro.bsuirschedule.feature.addSchedule.AddScheduleContainerPagerAdapter.FragmentType
import com.pechuro.bsuirschedule.feature.addSchedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.addSchedule.AddScheduleViewModel.State
import com.pechuro.bsuirschedule.feature.addSchedule.fragment.SuggestionItemInformation.EmployeeInfo
import com.pechuro.bsuirschedule.feature.addSchedule.fragment.SuggestionItemInformation.GroupInfo
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
        AddScheduleSuggestionsAdapter().also {
            it.setHasStableIds(true)
        }
    }

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
            setHasFixedSize(true)
        }

        addScheduleRetryButton.setSafeClickListener {
            val info = addScheduleSuggestionsRecyclerView.tag as? SuggestionItemInformation
            if (info != null) {
                loadSchedule(info)
            } else {
                viewModel.cancel()
            }
        }

        addScheduleCancelButton.setSafeClickListener {
            viewModel.cancel()
        }
    }

    private fun observeData() {
        viewModel.state.nonNull().observe(viewLifecycleOwner) {
            updateLayoutState(it)
        }
        when (scheduleType) {
            FragmentType.STUDENT -> {
                viewModel.allGroupsData.nonNull().observe(viewLifecycleOwner) {
                    suggestionsAdapter.submitList(it)
                }
            }
            FragmentType.EMPLOYEE -> {
                viewModel.allEmployeesData.nonNull().observe(viewLifecycleOwner) {
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