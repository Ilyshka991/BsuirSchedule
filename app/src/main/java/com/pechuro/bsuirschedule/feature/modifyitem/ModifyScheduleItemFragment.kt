package com.pechuro.bsuirschedule.feature.modifyitem

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.display.fragment.SCHEDULE_ITEM_DATE_FORMAT_PATTERN
import com.pechuro.bsuirschedule.feature.stafflist.StaffListItemSelectedEvent
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_modify_schedule_item.*
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ModifyScheduleItemFragmentArgs(
        val schedule: Schedule,
        val items: List<ScheduleItem>
) : Parcelable

class ModifyScheduleItemFragment : BaseFragment() {

    companion object {

        const val TAG = "ModifyScheduleItemFragment"

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        fun newInstance(args: ModifyScheduleItemFragmentArgs) = ModifyScheduleItemFragment().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    override val layoutId: Int = R.layout.fragment_modify_schedule_item

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ModifyScheduleItemViewModel::class)
    }

    private val args: ModifyScheduleItemFragmentArgs by lazy(LazyThreadSafetyMode.NONE) {
        parcelableOrException<ModifyScheduleItemFragmentArgs>(BUNDLE_ARGS)
    }

    private val dateFormatter = SimpleDateFormat(SCHEDULE_ITEM_DATE_FORMAT_PATTERN, Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fillParams()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(view?.findFocus()?.windowToken)
    }

    private fun initView() {
        modifyScheduleItemToolbar.title = getToolbarTitle()
        setParamsVisibility(args.schedule)
        setListeners()
    }

    private fun setParamsVisibility(schedule: Schedule) {
        val isClassesSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.EmployeeClasses
        modifyScheduleItemPriority.isVisible = isClassesSchedule
        modifyScheduleItemWeekday.isVisible = isClassesSchedule
        modifyScheduleItemWeekNumbers.isVisible = isClassesSchedule
        modifyScheduleItemDate.isVisible = !isClassesSchedule

        val isGroupSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.GroupExams
        modifyScheduleItemGroupsLabel.isVisible = !isGroupSchedule
        modifyScheduleItemGroupsChips.isVisible = !isGroupSchedule
        modifyScheduleItemEmployeesLabel.isVisible = isGroupSchedule
        modifyScheduleItemEmployeesChips.isVisible = isGroupSchedule
    }

    private fun setListeners() {
        modifyScheduleItemToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        modifyScheduleItemDoneButton.setSafeClickListener {
            viewModel.saveChanges()
        }
        modifyScheduleItemSubjectText.addTextListener {
            viewModel.dataProvider.subjectData.value = it
        }
        modifyScheduleItemNoteText.addTextListener {
            viewModel.dataProvider.noteData.value = it
        }
        modifyScheduleItemEmployeesLabel.setSafeClickListener {
        }
    }

    private fun fillParams() {
        val scheduleItems = args.items
        viewModel.dataProvider.init(args.schedule, scheduleItems)
        val subject = scheduleItems.firstOrNull()?.subject ?: ""
        modifyScheduleItemSubjectText.setText(subject)
        val note = scheduleItems.firstOrNull()?.note ?: ""
        modifyScheduleItemNoteText.setText(note)
    }

    private fun observeData() {
        EventBus.receive<StaffListItemSelectedEvent>(lifecycleScope) {

        }
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
        with(viewModel.dataProvider) {
            lessonTypeData.nonNull().observe(viewLifecycleOwner) { lessonType ->
                modifyScheduleItemType.setMessage(lessonType)
            }
            subgroupNumberData.nonNull().observe(viewLifecycleOwner) { subgroupNumber ->
                modifyScheduleItemSubgroupNumber.setMessage(subgroupNumber.formattedStringRes)
            }
            startTimeData.nonNull().observe(viewLifecycleOwner) { startTime ->
                modifyScheduleItemStartTime.setMessage(startTime)
            }
            endTimeData.nonNull().observe(viewLifecycleOwner) { endTime ->
                modifyScheduleItemEndTime.setMessage(endTime)
            }
            priorityData.nonNull().observe(viewLifecycleOwner) { priority ->
                modifyScheduleItemPriority.setMessage(priority.formattedStringRes)
            }
            weekDayData.nonNull().observe(viewLifecycleOwner) { weekDay ->
                modifyScheduleItemWeekday.setMessage(weekDay.getFormattedString(resources))
            }
            weekNumberData.nonNull().observe(viewLifecycleOwner) { weekNumbers ->
                val formattedWeekNumbers = weekNumbers.map { it.index + 1 }.sorted().joinToString { it.toString() }
                modifyScheduleItemWeekNumbers.setMessage(formattedWeekNumbers)
            }
            dateData.nonNull().observe(viewLifecycleOwner) { date ->
                modifyScheduleItemDate.setMessage(dateFormatter.format(date))
            }
            auditoriesData.nonNull().observe(viewLifecycleOwner) {

            }
            employeesData.nonNull().observe(viewLifecycleOwner) {

            }
            studentGroupsData.nonNull().observe(viewLifecycleOwner) {

            }
        }
    }

    private fun getToolbarTitle(): String {
        val scheduleItem = args.items.firstOrNull()
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
}