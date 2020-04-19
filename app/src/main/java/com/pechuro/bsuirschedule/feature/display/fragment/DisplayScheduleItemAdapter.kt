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
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.color
import com.pechuro.bsuirschedule.ext.formattedColorRes
import com.pechuro.bsuirschedule.ext.setSafeClickListener
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

private const val CLASSES_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_classes
private const val EXAMS_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_exam
private const val EMPTY_VIEW_TYPE_LAYOUT_ID = R.layout.item_display_schedule_empty

const val SCHEDULE_ITEM_DATE_FORMAT_PATTERN = "dd.MM.yyyy"

class DisplayScheduleItemAdapter(
        onClickCallback: (data: DisplayScheduleItem) -> Unit,
        onLongClickCallback: (data: DisplayScheduleItem) -> Unit
) : ListAdapter<DisplayScheduleItem, BaseViewHolder<DisplayScheduleItem>>(DIFF_CALLBACK) {

    private val clickListener = object : View.OnClickListener, View.OnLongClickListener {

        override fun onClick(view: View) {
            val data = view.tag as? DisplayScheduleItem? ?: return
            onClickCallback(data)
        }

        override fun onLongClick(view: View): Boolean {
            val data = view.tag as? DisplayScheduleItem? ?: return false
            onLongClickCallback(data)
            return true
        }
    }

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
        holder.itemView.apply {
            setSafeClickListener(onClickListener = clickListener)
            setOnLongClickListener(clickListener)
        }
    }

    override fun getItemId(position: Int) = getItem(position).scheduleItem?.id ?: RecyclerView.NO_ID

    private class ClassesViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem>(view) {

        override fun onBind(data: DisplayScheduleItem) {
            val scheduleItem = data.scheduleItem
            if (scheduleItem !is Lesson) return
            scheduleItem.run {
                displayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.formattedColorRes)
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
                    val weekNumbers = data.allScheduleItems.map { it.weekNumber }.sorted()
                    displayClassesWeeks.isVisible = weekNumbers.size != WeekNumber.TOTAL_COUNT
                    displayClassesWeeks.text = itemView.context.getString(
                            R.string.display_schedule_item_msg_week_numbers,
                            weekNumbers.formatWeekNumbers()
                    )
                }
                val info = when (scheduleItem) {
                    is Lesson.GroupLesson -> scheduleItem.employees.formatEmployees()
                    is Lesson.EmployeeLesson -> scheduleItem.studentGroups.formatGroupNumbers()
                }
                displayClassesInfo.text = info

            }
            itemView.tag = data
        }
    }

    private class ExamsViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Exams>(view) {

        private val dateFormatter = SimpleDateFormat(SCHEDULE_ITEM_DATE_FORMAT_PATTERN, Locale.getDefault())

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
            itemView.tag = data
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