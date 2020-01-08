package com.pechuro.bsuirschedule.feature.main.addschedule.fragment

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.os.bundleOf
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleDialogPagerAdapter.FragmentType
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleViewModel
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
        initViewModel(AddScheduleViewModel::class)
    }

    private val scheduleType: FragmentType by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getSerializable(ARG_SCHEDULE_TYPE) as FragmentType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            //     viewModel.loadSuggestions(scheduleType)
        }
        initView()
        observeData()
    }

    private fun initView() {
        val inputType = when (scheduleType) {
            FragmentType.STUDENT -> InputType.TYPE_CLASS_NUMBER
            FragmentType.EMPLOYEE -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        }
        addScheduleNameInput.inputType = inputType
        addScheduleDoneButton.setOnClickListener {
            loadSchedule()
        }
        addScheduleRetryButton.setOnClickListener {
            loadSchedule()
        }
    }

    private fun observeData() {
        /* viewModel.suggestions.observe(this,
                 Observer {
                     if (it != null) {
                         val adapter = ArrayAdapter<String>(
                                 viewDataBinding.textInput.context,
                                 R.layout.item_autocomplete_adapter,
                                 it)
                         viewDataBinding.textInput.setAdapter(adapter)
                     }
                 })*/
    }

    private fun loadSchedule() {
        val scheduleName = addScheduleNameInput.text.toString()

        val scheduleTypes = mutableListOf<ScheduleType>()
        if (addScheduleTypeClasses.isChecked) {
            val classesType = when (scheduleType) {
                FragmentType.STUDENT -> ScheduleType.STUDENT_CLASSES
                FragmentType.EMPLOYEE -> ScheduleType.EMPLOYEE_CLASSES
            }
            scheduleTypes.add(classesType)
        }
        if (addScheduleTypeExams.isChecked) {
            val examsType = when (scheduleType) {
                FragmentType.STUDENT -> ScheduleType.STUDENT_EXAMS
                FragmentType.EMPLOYEE -> ScheduleType.EMPLOYEE_EXAMS
            }
            scheduleTypes.add(examsType)
        }

       // viewModel.loadSchedule(scheduleName, scheduleTypes)
    }
}