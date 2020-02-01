package com.pechuro.bsuirschedule.feature.add.addschedule.fragment

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleContainerPagerAdapter.FragmentType
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleViewModel.State
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
        ArrayAdapter<String>(
                requireContext(),
                R.layout.item_autocomplete_adapter,
                mutableListOf()
        )
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

            setAdapter(suggestionsAdapter)

            addTextListener {
                addScheduleDoneButton.isEnabled = suggestionsAdapter.getPosition(it) != -1
            }

            onKeyboardClose = {
                viewModel.complete()
            }
        }

        addScheduleDoneButton.setOnClickListener {
            loadSchedule()
        }

        addScheduleRetryButton.setOnClickListener {
            loadSchedule()
        }

        addScheduleCancelButton.setOnClickListener {
            viewModel.cancel()
        }
    }

    private fun observeData() {
        viewModel.state.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is State.Idle -> {
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
                viewModel.allGroupNames.observeNonNull(viewLifecycleOwner) {
                    addGroupSuggestions(it)
                }
            }
            FragmentType.EMPLOYEE -> {
                viewModel.allEmployeeNames.observeNonNull(viewLifecycleOwner) {
                    addEmployeeSuggestions(it)
                }
            }
        }
    }

    private fun addGroupSuggestions(suggestions: List<Group>) {
        suggestionsAdapter.apply {
            clear()
            addAll(suggestions.map { it.number })
        }
    }

    private fun addEmployeeSuggestions(suggestions: List<Employee>) {
        suggestionsAdapter.apply {
            clear()
            addAll(suggestions.map { it.abbreviation })
        }
    }

    private fun loadSchedule() {
        val scheduleName = addScheduleNameInput.text.toString()
        val scheduleTypes = sequence {
            if (addScheduleChipClasses.isChecked) yield(ScheduleType.CLASSES)
            if (addScheduleChipExams.isChecked) yield(ScheduleType.EXAMS)
        }.toList()

       // viewModel.loadSchedule(scheduleName, scheduleTypes)
    }
}