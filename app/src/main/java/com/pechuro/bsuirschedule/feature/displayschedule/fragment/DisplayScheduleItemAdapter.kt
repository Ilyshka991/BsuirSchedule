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
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_group_day_classes.*

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


class DisplayScheduleItemAdapter : ListAdapter<DisplayScheduleItem, BaseViewHolder<DisplayScheduleItem>>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DisplayScheduleItem.GroupDayClasses -> GROUP_DAY_CLASSES_VIEW_TYPE
        else -> throw IllegalStateException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DisplayScheduleItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            GROUP_DAY_CLASSES_VIEW_TYPE -> GroupDayClassesViewHolder(view)
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
}