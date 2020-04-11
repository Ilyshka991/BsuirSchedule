package com.pechuro.bsuirschedule.feature.modifyScheduleItem

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*

class ModifyScheduleItemFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_modify_schedule_item

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ModifyScheduleItemViewModel::class)
    }
    private val args: ModifyScheduleItemFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        modifyScheduleItemToolbar.apply {
            title = getToolbarTitle()
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        modifyScheduleItemDoneButton.setSafeClickListener {
            viewModel.saveChanges(args.schedule, getResultScheduleItem())
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

    private fun getResultScheduleItem(): ScheduleItem {
        return Lesson.GroupLesson(
                id = 0,
                subject = "",
                subgroupNumber = SubgroupNumber.ALL,
                lessonType = "aa",
                note = "sad",
                startTime = "13.45",
                endTime = "13.46",
                auditories = emptyList(),
                isAddedByUser = true,
                priority = LessonPriority.HIGH,
                weekDay = WeekDay.SATURDAY,
                weekNumber = WeekNumber.FIRST,
                employees = emptyList()
        )
    }
}

