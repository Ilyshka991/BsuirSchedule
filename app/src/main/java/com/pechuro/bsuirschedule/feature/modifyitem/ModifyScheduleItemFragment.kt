package com.pechuro.bsuirschedule.feature.modifyitem

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.hideKeyboard
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*
import java.util.*

class ModifyScheduleItemFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_modify_schedule_item

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ModifyScheduleItemViewModel::class)
    }
    private val args: ModifyScheduleItemFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(view?.findFocus()?.windowToken)
    }

    private fun initView() {
        modifyScheduleItemToolbar.apply {
            title = getToolbarTitle()
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        modifyScheduleItemDoneButton.setSafeClickListener {
            val isInputValid = validate()
            modifyScheduleItemDoneButton.isEnabled = isInputValid
            if (isInputValid) {
                viewModel.saveChanges(args.schedule, getResultScheduleItem())
            }
        }
    }

    private fun observeData() {
        viewModel.state.nonNull().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModifyScheduleItemViewModel.State.Saving -> {
                    modifyScheduleItemDoneButton.isEnabled = false
                }
                is ModifyScheduleItemViewModel.State.Complete -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun getToolbarTitle(): String {
        val scheduleItem = args.item
        return if (scheduleItem == null) {
            val scheduleTypeResId = when (args.schedule) {
                is Schedule.GroupClasses, is Schedule.EmployeeClasses -> R.string.modify_schedule_item_msg_lesson
                is Schedule.GroupExams, is Schedule.EmployeeExams -> R.string.modify_schedule_item_msg_exam
            }
            val scheduleType = getString(scheduleTypeResId)
            getString(R.string.modify_schedule_item_title_add, scheduleType)
        } else {
            getString(R.string.modify_schedule_item_title_edit, scheduleItem.subject)
        }
    }

    private fun validate(): Boolean {
        return true
    }

    private fun getResultScheduleItem(): ScheduleItem {
        val id = args.item?.id ?: 0
        val subject = ""
        val subgroupNumber = SubgroupNumber.ALL
        val lessonType = "aa"
        val note = "sad"
        val startTime = "13.45"
        val endTime = "13.46"
        val auditories: List<Auditory> = emptyList()
        val isAddedByUser = args.item == null
        return when (args.schedule) {
            is Schedule.GroupClasses -> Lesson.GroupLesson(
                    id = id,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    priority = LessonPriority.HIGH,
                    weekDay = WeekDay.SATURDAY,
                    weekNumber = WeekNumber.FIRST,
                    employees = emptyList()
            )
            is Schedule.EmployeeClasses -> Lesson.EmployeeLesson(
                    id = id,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    priority = LessonPriority.HIGH,
                    weekDay = WeekDay.SATURDAY,
                    weekNumber = WeekNumber.FIRST,
                    studentGroups = emptyList()
            )
            is Schedule.GroupExams -> Exam.GroupExam(
                    id = id,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = Date(),
                    employees = emptyList()
            )
            is Schedule.EmployeeExams -> Exam.EmployeeExam(
                    id = id,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = Date(),
                    studentGroups = emptyList()
            )
        }
    }
}