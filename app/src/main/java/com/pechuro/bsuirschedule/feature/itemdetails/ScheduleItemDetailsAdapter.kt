package com.pechuro.bsuirschedule.feature.itemdetails

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.formattedString
import com.pechuro.bsuirschedule.feature.itemdetails.ScheduleItemDetailsInfo.*
import kotlinx.android.synthetic.main.item_schedule_details_auditories.*
import kotlinx.android.synthetic.main.item_schedule_details_employees.*
import kotlinx.android.synthetic.main.item_schedule_details_groups.*
import kotlinx.android.synthetic.main.item_schedule_details_lesson_date.*
import kotlinx.android.synthetic.main.item_schedule_details_note.*
import kotlinx.android.synthetic.main.item_schedule_details_time.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleItemDetailsInfo>() {

    override fun areItemsTheSame(
            oldItem: ScheduleItemDetailsInfo,
            newItem: ScheduleItemDetailsInfo
    ) = oldItem == newItem

    override fun areContentsTheSame(
            oldItem: ScheduleItemDetailsInfo,
            newItem: ScheduleItemDetailsInfo
    ) = oldItem == newItem
}

private const val EMPLOYEE_INFO_TYPE_LAYOUT_RES = R.layout.item_schedule_details_employees
private const val GROUP_INFO_TYPE_LAYOUT_RES = R.layout.item_schedule_details_groups
private const val TIME_TYPE_LAYOUT_RES = R.layout.item_schedule_details_time
private const val LESSON_TYPE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_lesson_type
private const val LESSON_DATE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_lesson_date
private const val EXAM_DATE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_exam_date
private const val SUBGROUP_NUMBER_TYPE_LAYOUT_RES = R.layout.item_schedule_details_subgroup
private const val PRIORITY_TYPE_LAYOUT_RES = R.layout.item_schedule_details_priority
private const val AUDITORY_INFO_TYPE_LAYOUT_RES = R.layout.item_schedule_details_auditories
private const val NOTE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_note

class ScheduleItemDetailsAdapter : ListAdapter<ScheduleItemDetailsInfo, BaseViewHolder<ScheduleItemDetailsInfo>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeLayoutRes: Int): BaseViewHolder<ScheduleItemDetailsInfo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewTypeLayoutRes, parent, false)
        return when (viewTypeLayoutRes) {
            EMPLOYEE_INFO_TYPE_LAYOUT_RES -> EmployeeInfoViewHolder(view)
            GROUP_INFO_TYPE_LAYOUT_RES -> GroupInfoViewHolder(view)
            TIME_TYPE_LAYOUT_RES -> TimeViewHolder(view)
            LESSON_TYPE_TYPE_LAYOUT_RES -> LessonTypeViewHolder(view)
            LESSON_DATE_TYPE_LAYOUT_RES -> LessonDateViewHolder(view)
            EXAM_DATE_TYPE_LAYOUT_RES -> ExamDateViewHolder(view)
            SUBGROUP_NUMBER_TYPE_LAYOUT_RES -> SubgroupViewHolder(view)
            PRIORITY_TYPE_LAYOUT_RES -> PriorityViewHolder(view)
            AUDITORY_INFO_TYPE_LAYOUT_RES -> AuditoryInfoViewHolder(view)
            NOTE_TYPE_LAYOUT_RES -> NoteViewHolder(view)
            else -> throw IllegalArgumentException("Not supported type: $viewTypeLayoutRes")
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is EmployeeInfo -> EMPLOYEE_INFO_TYPE_LAYOUT_RES
        is GroupInfo -> GROUP_INFO_TYPE_LAYOUT_RES
        is Time -> TIME_TYPE_LAYOUT_RES
        is LessonType -> LESSON_TYPE_TYPE_LAYOUT_RES
        is LessonDate -> LESSON_DATE_TYPE_LAYOUT_RES
        is ExamDate -> EXAM_DATE_TYPE_LAYOUT_RES
        is Subgroup -> SUBGROUP_NUMBER_TYPE_LAYOUT_RES
        is Priority -> PRIORITY_TYPE_LAYOUT_RES
        is AuditoryInfo -> AUDITORY_INFO_TYPE_LAYOUT_RES
        is Note -> NOTE_TYPE_LAYOUT_RES
    }

    override fun onBindViewHolder(
            holder: BaseViewHolder<ScheduleItemDetailsInfo>,
            position: Int
    ) = holder.onBind(getItem(position))

    private class EmployeeInfoViewHolder(view: View) : BaseViewHolder<EmployeeInfo>(view) {
        override fun onBind(data: EmployeeInfo) {
            scheduleItemDetailsEmployeesInfo.setEmployees(data.employees)
        }
    }

    private class GroupInfoViewHolder(view: View) : BaseViewHolder<GroupInfo>(view) {
        override fun onBind(data: GroupInfo) {
            scheduleItemDetailsGroupsInfo.setGroups(data.groups)
        }
    }

    private class TimeViewHolder(view: View) : BaseViewHolder<Time>(view) {
        override fun onBind(data: Time) {
            lessonDetailsTime.text = with(data) { "${startTime.formattedString} - ${endTime.formattedString}" }
        }
    }

    private class LessonTypeViewHolder(view: View) : BaseViewHolder<LessonType>(view) {
        override fun onBind(data: LessonType) {

        }
    }

    private class LessonDateViewHolder(view: View) : BaseViewHolder<LessonDate>(view) {
        override fun onBind(data: LessonDate) {
            lessonDetailsWeeks.text = getWeeksText(itemView.context, data.weeks)
        }

        private fun getWeeksText(context: Context, weeks: List<WeekNumber>): CharSequence {
            val builder = SpannableStringBuilder()
            builder.bold {
                val prefix = context.getString(R.string.lesson_details_weeks)
                append(prefix)
            }
            builder.append(" ")
            builder.append(weeks.joinToString { it.formattedString })
            return builder.toSpannable()
        }
    }

    private class ExamDateViewHolder(view: View) : BaseViewHolder<ExamDate>(view) {
        override fun onBind(data: ExamDate) {

        }
    }

    private class SubgroupViewHolder(view: View) : BaseViewHolder<Subgroup>(view) {
        override fun onBind(data: Subgroup) {

        }
    }

    private class PriorityViewHolder(view: View) : BaseViewHolder<Priority>(view) {
        override fun onBind(data: Priority) {

        }
    }

    private class AuditoryInfoViewHolder(view: View) : BaseViewHolder<AuditoryInfo>(view) {
        override fun onBind(data: AuditoryInfo) {
            val auditory = data.auditory

            lessonDetailsLocationAuditoryName.text = with(auditory) { "$name-${building.name}" }
            lessonDetailsLocationAuditoryType.text = auditory.auditoryType.name

            lessonDetailsLocationAuditoryNote.isVisible = auditory.note.isNotEmpty()
            lessonDetailsLocationAuditoryNote.text = auditory.note

            val department = auditory.department
            lessonDetailsLocationAuditoryDepartment.isVisible = department != null
            lessonDetailsLocationAuditoryDepartment.text = department?.name
        }
    }

    private class NoteViewHolder(view: View) : BaseViewHolder<Note>(view) {
        override fun onBind(data: Note) {
            lessonDetailsNoteText.apply {
                tag = data
                if (data.note != text?.toString()) {
                    setText(data.note)
                }
            }
        }
    }
}