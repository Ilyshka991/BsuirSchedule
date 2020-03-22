package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_employee_day_classes.*
import kotlinx.android.synthetic.main.item_display_group_day_classes.*
import kotlinx.android.synthetic.main.item_display_group_week_classes.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DisplayScheduleItem>() {

    override fun areItemsTheSame(
            oldItem: DisplayScheduleItem,
            newItem: DisplayScheduleItem
    ) = oldItem === newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
            oldItem: DisplayScheduleItem,
            newItem: DisplayScheduleItem
    ) = oldItem == newItem
}


@LayoutRes private const val GROUP_DAY_CLASSES_VIEW_TYPE = R.layout.item_display_group_day_classes
@LayoutRes private const val GROUP_WEEK_CLASSES_VIEW_TYPE = R.layout.item_display_group_week_classes
@LayoutRes private const val EMPLOYEE_DAY_CLASSES_VIEW_TYPE = R.layout.item_display_employee_day_classes


class DisplayScheduleItemAdapter : ListAdapter<DisplayScheduleItem, BaseViewHolder<DisplayScheduleItem>>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DisplayScheduleItem.GroupDayClasses -> GROUP_DAY_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.GroupWeekClasses -> GROUP_WEEK_CLASSES_VIEW_TYPE
        is DisplayScheduleItem.EmployeeDayClasses -> EMPLOYEE_DAY_CLASSES_VIEW_TYPE
        else -> throw IllegalStateException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DisplayScheduleItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            GROUP_DAY_CLASSES_VIEW_TYPE -> GroupDayClassesViewHolder(view)
            GROUP_WEEK_CLASSES_VIEW_TYPE -> GroupWeekClassesViewHolder(view)
            EMPLOYEE_DAY_CLASSES_VIEW_TYPE -> EmployeeDayClassesViewHolder(view)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DisplayScheduleItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position).scheduleItem.id


    private inner class GroupDayClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.GroupDayClasses>(view) {

        override fun onBind(data: DisplayScheduleItem.GroupDayClasses) {
            with(data.scheduleItem) {
                displayGroupDayClassesLessonType.text = lessonType
                displayGroupDayClassesTitle.text = subject
                displayGroupDayClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayGroupDayClassesSubGroup.apply {
                    if (subgroupNumber != 0) {
                        text = context.getString(R.string.display_schedule_item_subgroup, subgroupNumber)
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
                displayGroupDayClassesEmployees.text = employees.joinToString(separator = ",") { it.abbreviation }
                displayGroupDayClassesStartTime.text = startTime
                displayGroupDayClassesEndTime.text = endTime
                displayGroupDayClassesNotes.apply {
                    if (note.isNotEmpty() && note.isNotBlank()) {
                        text = note
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    private inner class GroupWeekClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.GroupWeekClasses>(view) {

        override fun onBind(data: DisplayScheduleItem.GroupWeekClasses) {
            with(data.scheduleItem) {
                displayGroupWeekClassesLessonType.text = lessonType
                displayGroupWeekClassesTitle.text = subject
                displayGroupWeekClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayGroupWeekClassesSubGroup.apply {
                    if (subgroupNumber != 0) {
                        text = context.getString(R.string.display_schedule_item_subgroup, subgroupNumber)
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
                displayGroupWeekClassesWeekNumbers.apply {
                    if (data.weekNumbers.size == WeekNumber.TOTAL_COUNT) {
                        visibility = View.GONE
                    } else {
                        text = context.getString(
                                R.string.display_schedule_item_week_numbers,
                                data.weekNumbers.joinToString(separator = ",") { it.index.toString() }
                        )
                        visibility = View.VISIBLE
                    }
                }
                displayGroupWeekClassesEmployees.text = employees.joinToString(separator = ",") { it.abbreviation }
                displayGroupWeekClassesStartTime.text = startTime
                displayGroupWeekClassesEndTime.text = endTime
                displayGroupWeekClassesNotes.apply {
                    if (note.isNotEmpty() && note.isNotBlank()) {
                        text = note
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    private inner class EmployeeDayClassesViewHolder(view: View) :
            BaseViewHolder<DisplayScheduleItem.EmployeeDayClasses>(view) {

        override fun onBind(data: DisplayScheduleItem.EmployeeDayClasses) {
            with(data.scheduleItem) {
                displayEmployeeDayClassesLessonType.text = lessonType
                displayEmployeeDayClassesTitle.text = subject
                displayEmployeeDayClassesAuditories.text = auditories.joinToString(separator = ",") { it.name }
                displayEmployeeDayClassesSubGroup.apply {
                    if (subgroupNumber != 0) {
                        text = context.getString(R.string.display_schedule_item_subgroup, subgroupNumber)
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
                displayEmployeeDayClassesGroups.text = studentGroups.joinToString(separator = ",") { it.number }
                displayEmployeeDayClassesStartTime.text = startTime
                displayEmployeeDayClassesEndTime.text = endTime
                displayEmployeeDayClassesNotes.apply {
                    if (note.isNotEmpty() && note.isNotBlank()) {
                        text = note
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }
    }
}