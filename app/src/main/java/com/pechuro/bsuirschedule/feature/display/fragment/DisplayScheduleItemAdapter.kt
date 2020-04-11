package com.pechuro.bsuirschedule.feature.display.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
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
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_schedule_classes.*
import kotlinx.android.synthetic.main.item_display_schedule_exam.*
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

private class OnScheduleItemClickListener : View.OnClickListener {
    override fun onClick(view: View) {
        val scheduleItem = view.tag as? ScheduleItem? ?: return
        EventBus.send(DisplayScheduleEvent.OpenScheduleItemDetails(scheduleItem))
    }
}

private class OnScheduleItemLongClickListener : View.OnLongClickListener {
    override fun onLongClick(view: View): Boolean {
        val scheduleItem = view.tag as? ScheduleItem? ?: return false
        EventBus.send(DisplayScheduleEvent.OpenScheduleItemOptions(scheduleItem))
        return true
    }
}

private const val CLASSES_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_classes
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
            itemView.setSafeClickListener(onClickListener = OnScheduleItemClickListener())
            itemView.setOnLongClickListener(OnScheduleItemLongClickListener())
        }

        override fun onBind(data: DisplayScheduleItem) {
            val scheduleItem = data.scheduleItem
            if (scheduleItem !is Lesson) return
            scheduleItem.run {
                displayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.colorRes)
                ViewCompat.setBackgroundTintList(displayClassesLessonType, ColorStateList.valueOf(lessonTypeColor))
                displayClassesTitle.text = subject
                displayClassesAuditories.text = auditories.formatAuditories()
                displayClassesSubgroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayClassesSubgroup.text = itemView.context.getString(R.string.display_schedule_item_msg_subgroup, subgroupNumber.value)
                displayClassesStartTime.text = startTime
                displayClassesEndTime.text = endTime
                displayClassesNote.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayClassesNote.text = note
                if (data is DisplayScheduleItem.WeekClasses) {
                    displayClassesWeeks.isVisible = data.weekNumbers.size != WeekNumber.TOTAL_COUNT
                    displayClassesWeeks.text = itemView.context.getString(
                            R.string.display_schedule_item_msg_week_numbers,
                            data.weekNumbers.formatWeekNumbers()
                    )
                }
                val info = when (scheduleItem) {
                    is Lesson.GroupLesson -> scheduleItem.employees.formatEmployees()
                    is Lesson.EmployeeLesson -> scheduleItem.studentGroups.formatGroupNumbers()
                }
                displayClassesInfo.text = info

            }
            itemView.tag = scheduleItem
        }
    }

    private class ExamsViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Exams>(view) {

        private val dateFormatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

        init {
            itemView.setSafeClickListener(onClickListener = OnScheduleItemClickListener())
            itemView.setOnLongClickListener(OnScheduleItemLongClickListener())
        }

        override fun onBind(data: DisplayScheduleItem.Exams) {
            val scheduleItem = data.scheduleItem
            data.scheduleItem.run {
                displayExamStartTime.text = startTime
                displayExamAuditories.text = auditories.formatAuditories()
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
                    is Exam.EmployeeExam -> scheduleItem.studentGroups.formatGroupNumbers()
                    is Exam.GroupExam -> scheduleItem.employees.formatEmployees()
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

private fun List<WeekNumber>.formatWeekNumbers() = joinToString(separator = ",") { it.index.plus(1).toString() }

private fun List<Auditory>.formatAuditories() = joinToString(separator = ",") { "${it.name}-${it.building.name}" }

private fun List<Employee>.formatEmployees() = joinToString(separator = ",") { it.abbreviation }

private fun List<Group>.formatGroupNumbers() = asSequence()
        .map { it.number }
        .filter { it.isNotEmpty() }
        .map { it.take(it.lastIndex) to it }
        .groupBy(keySelector = { it.first }, valueTransform = { it.second })
        .map { (commonPart, groupNumbers) ->
            if (groupNumbers.size > 1) {
                "${commonPart}X"
            } else {
                groupNumbers.firstOrNull() ?: ""
            }
        }
        .joinToString(separator = ",") { it }