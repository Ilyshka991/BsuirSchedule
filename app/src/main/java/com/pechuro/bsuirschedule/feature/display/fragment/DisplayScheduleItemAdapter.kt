package com.pechuro.bsuirschedule.feature.display.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.item_display_schedule_classes.*
import kotlinx.android.synthetic.main.item_display_schedule_exam.*
import java.util.*
import java.util.concurrent.TimeUnit

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

    var dataUpdatesCount = 0
        private set

    override fun submitList(list: List<DisplayScheduleItem>?) {
        dataUpdatesCount++
        super.submitList(list)
    }

    override fun submitList(list: List<DisplayScheduleItem>?, commitCallback: Runnable?) {
        dataUpdatesCount++
        super.submitList(list, commitCallback)
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
            val defaultItemPadding = itemView.context.dimenPx(R.dimen.display_schedule_item_default_padding)
            scheduleItem.run {
                displayClassesLessonType.text = lessonType
                val lessonTypeColor = itemView.context.color(priority.formattedColorRes)
                ViewCompat.setBackgroundTintList(displayClassesLessonType, ColorStateList.valueOf(lessonTypeColor))
                displayClassesTitle.text = subject
                displayClassesTitle.updatePadding(right = if (subject.isNotEmpty()) defaultItemPadding else 0)
                val formattedAuditories = auditories.formatAuditories()
                displayClassesAuditories.text = formattedAuditories
                displayClassesAuditories.updatePadding(right = if (formattedAuditories.isNotEmpty()) defaultItemPadding else 0)
                displayClassesSubgroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayClassesSubgroup.text = itemView.context.getString(R.string.display_schedule_item_msg_subgroup, subgroupNumber.value)
                displayClassesStartTime.text = startTime.formattedString
                displayClassesEndTime.text = endTime.formattedString
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

        override fun onBind(data: DisplayScheduleItem.Exams) {
            val scheduleItem = data.scheduleItem
            data.scheduleItem.run {
                displayExamStartTime.text = startTime.formattedString
                displayExamAuditories.text = auditories.formatAuditories()
                displayExamTitle.text = subject
                displayExamDate.text = date.formattedString
                displayExamSubgroup.isVisible = subgroupNumber != SubgroupNumber.ALL
                displayExamSubgroup.text = itemView.context.getString(
                        R.string.display_schedule_item_msg_subgroup,
                        subgroupNumber.value
                )
                displayExamType.text = lessonType
                displayExamsNote.isVisible = note.isNotEmpty() && note.isNotBlank()
                displayExamsNote.text = note
                val isExam = lessonType.toLowerCase(Locale.getDefault()) == "экзамен"
                displayExamLeftDaysIndicator.isVisible = isExam
                if (isExam) {
                    val leftDaysIndicatorColor = itemView.context.color(getLeftDaysIndicatorColorRes(date, startTime))
                    displayExamLeftDaysIndicator.setBackgroundColor(leftDaysIndicatorColor)
                }
                val info = when (scheduleItem) {
                    is Exam.EmployeeExam -> scheduleItem.studentGroups.formatGroupNumbers()
                    is Exam.GroupExam -> scheduleItem.employees.formatEmployees()
                }
                displayExamInfo.text = info

            }
            itemView.tag = data
        }

        @ColorRes
        private fun getLeftDaysIndicatorColorRes(examDate: LocalDate, examTime: LocalTime): Int {
            val dateDiffMs = examDate.toDate().time - LocalDate.current().toDate().time
            val daysLeft = TimeUnit.DAYS.convert(dateDiffMs, TimeUnit.MILLISECONDS)
            val timeDiffMs = examTime.toDate().time - LocalTime.current().toDate().time
            return when {
                daysLeft < 0L -> R.color.transparent
                daysLeft == 0L && timeDiffMs < 0 -> R.color.transparent
                daysLeft == 0L -> R.color.amber_900
                daysLeft < 3 -> R.color.amber_800
                daysLeft < 5 -> R.color.amber_700
                daysLeft < 7 -> R.color.amber_600
                daysLeft < 10 -> R.color.amber_500
                daysLeft < 12 -> R.color.amber_400
                daysLeft < 15 -> R.color.amber_300
                daysLeft < 20 -> R.color.amber_200
                else -> R.color.amber_100
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<DisplayScheduleItem.Empty>(view) {

        override fun onBind(data: DisplayScheduleItem.Empty) {}
    }
}

fun List<WeekNumber>.formatWeekNumbers() = joinToString(separator = ",") { it.formattedString }

fun List<Auditory>.formatAuditories() = joinToString(separator = ",") { it.formattedName }

fun List<Employee>.formatEmployees() = joinToString(separator = ",") { it.abbreviation }

fun List<Group>.formatGroupNumbers() = asSequence()
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