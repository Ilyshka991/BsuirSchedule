package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.color
import com.pechuro.bsuirschedule.ext.colorRes
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_schedule_employee_day_classes.*
import kotlinx.android.synthetic.main.item_display_schedule_employee_exams.*
import kotlinx.android.synthetic.main.item_display_schedule_group_day_classes.*
import kotlinx.android.synthetic.main.item_display_schedule_group_exams.*
import kotlinx.android.synthetic.main.item_display_schedule_group_week_classes.*
import java.text.SimpleDateFormat
import java.util.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DisplayScheduleItem>() {

    override fun areItemsTheSame(
            oldItem: DisplayScheduleItem,
            newItem: DisplayScheduleItem
    ) = oldItem.scheduleItem?.id == newItem.scheduleItem?.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
            oldItem: DisplayScheduleItem,
            newItem: DisplayScheduleItem
    ) = oldItem == newItem
}

class DisplayScheduleItemAdapter : ListAdapter<DisplayScheduleItem, BaseViewHolder<DisplayScheduleItem>>(DIFF_CALLBACK) {

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"

        private const val GROUP_DAY_CLASSES_VIEW_TYPE = R.layout.item_display_schedule_group_day_classes
        private const val GROUP_WEEK_CLASSES_VIEW_TYPE = R.layout.item_display_schedule_group_week_classes
        private const val GROUP_EXAMS_VIEW_TYPE = R.layout.item_display_schedule_group_exams
        private const val EMPLOYEE_DAY_CLASSES_VIEW_TYPE = R.layout.item_display_schedule_employee_day_classes
        private const val EMPLOYEE_WEEK_CLASSES_VIEW_TYPE = R.layout.item_display_schedule_employee_week_classes
        private const val EMPLOYEE_EXAMS_VIEW_TYPE = R.layout.item_display_schedule_employee_exams
        private const val EMPTY_VIEW_TYPE = R.layout.item_display_schedule_empty
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DisplayScheduleItem.GroupDayClasses -> GROUP_DAY_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.GroupWeekClasses -> GROUP_WEEK_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.GroupExams -> GROUP_EXAMS_VIEW_TYPE
        is DisplayScheduleItem.EmployeeDayClasses -> EMPLOYEE_DAY_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.EmployeeWeekClasses -> EMPLOYEE_WEEK_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.EmployeeExams -> EMPLOYEE_EXAMS_VIEW_TYPE
        is DisplayScheduleItem.Empty -> EMPTY_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DisplayScheduleItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            GROUP_DAY_CLASSES_VIEW_TYPE -> GroupDayClassesViewHolder(view)
            GROUP_WEEK_CLASSES_VIEW_TYPE -> GroupWeekClassesViewHolder(view)
            GROUP_EXAMS_VIEW_TYPE -> GroupExamsViewHolder(view)
            EMPLOYEE_DAY_CLASSES_VIEW_TYPE -> EmployeeDayClassesViewHolder(view)
            EMPLOYEE_WEEK_CLASSES_VIEW_TYPE -> EmployeeWeekClassesViewHolder(view)
            EMPLOYEE_EXAMS_VIEW_TYPE -> EmployeeExamsViewHolder(view)
            EMPTY_VIEW_TYPE -> EmptyViewHolder(view)
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DisplayScheduleItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position).scheduleItem?.id ?: RecyclerView.NO_ID

    private class GroupDayClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.GroupDayClasses>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.GroupDayClasses) {
            data.scheduleItem.run {
                displayGroupDayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                displayGroupDayClassesLessonType.setTextColor(lessonTypeColor)
                displayGroupDayClassesTitle.text = subject
                displayGroupDayClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayGroupDayClassesSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayGroupDayClassesSubGroup.text = itemView.context.getString(R.string.display_schedule_item_msg_subgroup, subgroupNumber.value)
                displayGroupDayClassesEmployees.text = employees.joinToString(separator = ",") { it.abbreviation }
                displayGroupDayClassesStartTime.text = startTime
                displayGroupDayClassesEndTime.text = endTime
                displayGroupDayClassesNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayGroupDayClassesNotes.text = note
            }
            itemView.tag = data.scheduleItem
        }
    }

    private class GroupWeekClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.GroupWeekClasses>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.GroupWeekClasses) {
            data.scheduleItem.run {
                displayGroupWeekClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                displayGroupWeekClassesLessonType.setTextColor(lessonTypeColor)
                displayGroupWeekClassesTitle.text = subject
                displayGroupWeekClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayGroupWeekClassesSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayGroupWeekClassesSubGroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayGroupWeekClassesWeekNumbers.isVisible = data.weekNumbers.size != WeekNumber.TOTAL_COUNT
                displayGroupWeekClassesWeekNumbers.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_week_numbers,
                        data.weekNumbers.joinToString(separator = ",") { it.index.toString() }
                )
                displayGroupWeekClassesEmployees.text = employees.joinToString(separator = ",") { it.abbreviation }
                displayGroupWeekClassesStartTime.text = startTime
                displayGroupWeekClassesEndTime.text = endTime
                displayGroupWeekClassesNotes.text = note
                displayGroupWeekClassesNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
            }
            itemView.tag = data.scheduleItem
        }
    }

    private class GroupExamsViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.GroupExams>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }
        private val dateFormatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.GroupExams) {
            data.scheduleItem.run {
                displayGroupExamsStartTime.text = startTime
                displayGroupExamsAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayGroupExamsTitle.text = subject
                displayGroupExamsDate.text = dateFormatter.format(date)
                displayGroupExamsSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayGroupExamsSubGroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayGroupExamsLessonType.text = lessonType
                displayGroupExamsEmployees.text = employees.joinToString(separator = ",") { it.abbreviation }
                displayGroupExamsNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayGroupExamsNotes.text = note
            }
            itemView.tag = data.scheduleItem
        }
    }

    private class EmployeeDayClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.EmployeeDayClasses>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.EmployeeDayClasses) {
            data.scheduleItem.run {
                displayEmployeeDayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                displayEmployeeDayClassesLessonType.setTextColor(lessonTypeColor)
                displayEmployeeDayClassesTitle.text = subject
                displayEmployeeDayClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayEmployeeDayClassesSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayEmployeeDayClassesSubGroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayEmployeeDayClassesGroups.text = studentGroups.joinToString(separator = ",") { it.number }
                displayEmployeeDayClassesStartTime.text = startTime
                displayEmployeeDayClassesEndTime.text = endTime
                displayEmployeeDayClassesNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayEmployeeDayClassesNotes.text = note
            }
            itemView.tag = data.scheduleItem
        }
    }

    private class EmployeeWeekClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.EmployeeWeekClasses>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.EmployeeWeekClasses) {
            data.scheduleItem.run {
                displayEmployeeDayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                displayEmployeeDayClassesLessonType.setTextColor(lessonTypeColor)
                displayEmployeeDayClassesTitle.text = subject
                displayEmployeeDayClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayEmployeeDayClassesSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayEmployeeDayClassesSubGroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayGroupWeekClassesWeekNumbers.isVisible = data.weekNumbers.size != WeekNumber.TOTAL_COUNT
                displayGroupWeekClassesWeekNumbers.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_week_numbers,
                        data.weekNumbers.joinToString(separator = ",") { it.index.toString() }
                )
                displayEmployeeDayClassesGroups.text = studentGroups.joinToString(separator = ",") { it.number }
                displayEmployeeDayClassesStartTime.text = startTime
                displayEmployeeDayClassesEndTime.text = endTime
                displayEmployeeDayClassesNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayEmployeeDayClassesNotes.text = note
            }
            itemView.tag = data.scheduleItem
        }
    }

    private class EmployeeExamsViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.EmployeeExams>(view) {

        private val onClickListener = View.OnClickListener {
            val scheduleItem = it.tag as? ScheduleItem? ?: return@OnClickListener
            EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
        }
        private val dateFormatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

        init {
            itemView.setSafeClickListener(onClickListener = onClickListener)
        }

        override fun onBind(data: DisplayScheduleItem.EmployeeExams) {
            data.scheduleItem.run {
                displayEmployeeExamsStartTime.text = startTime
                displayEmployeeExamsAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayEmployeeExamsTitle.text = subject
                displayEmployeeExamsDate.text = dateFormatter.format(date)
                displayEmployeeExamsSubGroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayEmployeeExamsSubGroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayEmployeeExamsLessonType.text = lessonType
                displayEmployeeExamsGroups.text = studentGroups.joinToString(separator = ",") { it.number }
                displayEmployeeExamsNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayEmployeeExamsNotes.text = note
            }
            itemView.tag = data.scheduleItem
        }
    }

    private inner class EmptyViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Empty>(view) {

        override fun onBind(data: DisplayScheduleItem.Empty) {}
    }
}