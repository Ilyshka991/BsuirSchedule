package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.color
import com.pechuro.bsuirschedule.ext.colorRes
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_schedule_exam.*
import kotlinx.android.synthetic.main.item_display_schedule_group_classes.*
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

private class OnScheduleItemCLickListener : View.OnClickListener {
    override fun onClick(view: View) {
        val scheduleItem = view.tag as? ScheduleItem? ?: return
        EventBus.send(DisplayScheduleEvent.OpenScheduleItem(scheduleItem))
    }
}

private const val CLASSES_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_group_classes
private const val EXAMS_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_exam
private const val EMPTY_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_empty

private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"

class DisplayScheduleItemAdapter : ListAdapter<DisplayScheduleItem, BaseViewHolder<DisplayScheduleItem>>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DisplayScheduleItem.DayClasses, is DisplayScheduleItem.WeekClasses -> CLASSES_VIEW_TYPE_LAYOUT_ID
        is DisplayScheduleItem.Exams -> EXAMS_VIEW_TYPE_LAYOUT_ID
        is DisplayScheduleItem.Empty -> EMPTY_VIEW_TYPE_LAYOUT_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DisplayScheduleItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            CLASSES_VIEW_TYPE_LAYOUT_ID -> ClassesViewHolder(view)
            EXAMS_VIEW_TYPE_LAYOUT_ID -> ExamsViewHolder(view)
            EMPTY_VIEW_TYPE_LAYOUT_ID -> EmptyViewHolder(view)
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DisplayScheduleItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position).scheduleItem?.id ?: RecyclerView.NO_ID

    private class ClassesViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem>(view) {

        init {
            itemView.setSafeClickListener(onClickListener = OnScheduleItemCLickListener())
        }

        override fun onBind(data: DisplayScheduleItem) {
            val scheduleItem = data.scheduleItem
            if (scheduleItem !is Lesson) return
            scheduleItem.run {
                displayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                displayClassesLessonType.supportBackgroundTintList = ColorStateList.valueOf(lessonTypeColor)
                displayClassesTitle.text = subject
                displayClassesAuditories.text = auditories.joinToString(separator = ",") { "${it.name}-${it.building.name}" }
                displayClassesSubgroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayClassesSubgroup.text = itemView.context.getString(R.string.display_schedule_item_msg_subgroup, subgroupNumber.value)
                displayClassesStartTime.text = startTime
                displayClassesEndTime.text = endTime
                displayClassesNotes.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayClassesNotes.text = note
                if (data is DisplayScheduleItem.WeekClasses) {
                    displayClassesWeeks.isVisible = data.weekNumbers.size != WeekNumber.TOTAL_COUNT
                    displayClassesWeeks.text = itemView.context.getString(
                            R.string.display_schedule_item_msg_week_numbers,
                            data.weekNumbers.joinToString(separator = ",") { it.index.plus(1).toString() }
                    )
                }
                val info = when (scheduleItem) {
                    is Lesson.GroupLesson -> scheduleItem.employees.joinToString(separator = ",") { it.abbreviation }
                    is Lesson.EmployeeLesson -> scheduleItem.studentGroups.joinToString(separator = ",") { it.number }
                }
                displayClassesInfo.text = info

            }
            itemView.tag = scheduleItem
        }
    }

    private class ExamsViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Exams>(view) {

        private val dateFormatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

        init {
            itemView.setSafeClickListener(onClickListener = OnScheduleItemCLickListener())
        }

        override fun onBind(data: DisplayScheduleItem.Exams) {
            val scheduleItem = data.scheduleItem
            data.scheduleItem.run {
                displayExamStartTime.text = startTime
                displayExamAuditories.text = auditories.joinToString(separator = ",") { "${it.name}-${it.building.name}" }
                displayExamTitle.text = subject
                displayExamDate.text = dateFormatter.format(date)
                displayExamSubgroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayExamSubgroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayExamType.text = lessonType
                displayExamsNote.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayExamsNote.text = note
                val info = when (scheduleItem) {
                    is Exam.EmployeeExam -> scheduleItem.studentGroups.joinToString(separator = ",") { it.number }
                    is Exam.GroupExam -> scheduleItem.employees.joinToString(separator = ",") { it.abbreviation }
                    else -> ""
                }
                displayExamInfo.text = info

            }
            itemView.tag = scheduleItem
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Empty>(view) {

        override fun onBind(data: DisplayScheduleItem.Empty) {}
    }
}