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
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.entity.toDate
import com.pechuro.bsuirschedule.ext.formattedString
import com.pechuro.bsuirschedule.ext.formattedStringRes
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.itemdetails.ScheduleItemDetailsInfo.*
import kotlinx.android.synthetic.main.item_schedule_details_auditory_info.*
import kotlinx.android.synthetic.main.item_schedule_details_employees.*
import kotlinx.android.synthetic.main.item_schedule_details_exam_date.*
import kotlinx.android.synthetic.main.item_schedule_details_groups.*
import kotlinx.android.synthetic.main.item_schedule_details_lesson_date.*
import kotlinx.android.synthetic.main.item_schedule_details_note.*
import kotlinx.android.synthetic.main.item_schedule_details_priority.*
import kotlinx.android.synthetic.main.item_schedule_details_subgroup.*
import kotlinx.android.synthetic.main.item_schedule_details_time.*
import java.text.SimpleDateFormat
import java.util.*

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
private const val LESSON_DATE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_lesson_date
private const val EXAM_DATE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_exam_date
private const val SUBGROUP_NUMBER_TYPE_LAYOUT_RES = R.layout.item_schedule_details_subgroup
private const val PRIORITY_TYPE_LAYOUT_RES = R.layout.item_schedule_details_priority
private const val AUDITORY_HEADER_INFO_TYPE_LAYOUT_RES = R.layout.item_schedule_details_auditories_header
private const val AUDITORY_INFO_TYPE_LAYOUT_RES = R.layout.item_schedule_details_auditory_info
private const val NOTE_TYPE_LAYOUT_RES = R.layout.item_schedule_details_note

class ScheduleItemDetailsAdapter(
        val onPrioritySelected: (currentPriority: LessonPriority) -> Unit
) : ListAdapter<ScheduleItemDetailsInfo, BaseViewHolder<ScheduleItemDetailsInfo>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeLayoutRes: Int): BaseViewHolder<ScheduleItemDetailsInfo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewTypeLayoutRes, parent, false)
        return when (viewTypeLayoutRes) {
            EMPLOYEE_INFO_TYPE_LAYOUT_RES -> EmployeeInfoViewHolder(view)
            GROUP_INFO_TYPE_LAYOUT_RES -> GroupInfoViewHolder(view)
            TIME_TYPE_LAYOUT_RES -> TimeViewHolder(view)
            LESSON_DATE_TYPE_LAYOUT_RES -> LessonDateViewHolder(view)
            EXAM_DATE_TYPE_LAYOUT_RES -> ExamDateViewHolder(view)
            SUBGROUP_NUMBER_TYPE_LAYOUT_RES -> SubgroupViewHolder(view)
            PRIORITY_TYPE_LAYOUT_RES -> PriorityViewHolder(view)
            AUDITORY_HEADER_INFO_TYPE_LAYOUT_RES -> AuditoryInfoHeaderViewHolder(view)
            AUDITORY_INFO_TYPE_LAYOUT_RES -> AuditoryInfoViewHolder(view)
            NOTE_TYPE_LAYOUT_RES -> NoteViewHolder(view)
            else -> throw IllegalArgumentException("Not supported type: $viewTypeLayoutRes")
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is EmployeeInfo -> EMPLOYEE_INFO_TYPE_LAYOUT_RES
        is GroupInfo -> GROUP_INFO_TYPE_LAYOUT_RES
        is Time -> TIME_TYPE_LAYOUT_RES
        is LessonDate -> LESSON_DATE_TYPE_LAYOUT_RES
        is ExamDate -> EXAM_DATE_TYPE_LAYOUT_RES
        is Subgroup -> SUBGROUP_NUMBER_TYPE_LAYOUT_RES
        is Priority -> PRIORITY_TYPE_LAYOUT_RES
        is AuditoryInfoHeader -> AUDITORY_HEADER_INFO_TYPE_LAYOUT_RES
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
            scheduleItemDetailsTime.text = with(data) {
                "${startTime.formattedString} - ${endTime.formattedString}"
            }
        }
    }

    private class LessonDateViewHolder(view: View) : BaseViewHolder<LessonDate>(view) {

        override fun onBind(data: LessonDate) {
            scheduleItemDetailsWeeks.text = itemView.context.getFormattedWeeksText(data.weeks)
        }

        private fun Context.getFormattedWeeksText(weeks: List<WeekNumber>): CharSequence {
            val builder = SpannableStringBuilder()
            builder.bold {
                val prefix = getString(R.string.item_details_msg_weeks)
                append(prefix)
            }
            builder.append(" ")
            builder.append(weeks.joinToString { it.formattedString })
            return builder.toSpannable()
        }
    }

    private class ExamDateViewHolder(view: View) : BaseViewHolder<ExamDate>(view) {

        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        override fun onBind(data: ExamDate) {
            val date = data.date.toDate()
            scheduleItemDetailsExamDate.text = dateFormatter.format(date)
        }
    }

    private class SubgroupViewHolder(view: View) : BaseViewHolder<Subgroup>(view) {

        override fun onBind(data: Subgroup) {
            scheduleItemDetailsSubgroupNumber.text = itemView.context.getString(data.subgroupNumber.formattedStringRes)
        }
    }

    private inner class PriorityViewHolder(view: View) : BaseViewHolder<Priority>(view) {

        init {
            scheduleItemDetailsPriority.setSafeClickListener {
                val currentPriority = it.tag as? LessonPriority ?: return@setSafeClickListener
                onPrioritySelected(currentPriority)
            }
        }

        override fun onBind(data: Priority) {
            scheduleItemDetailsPriority.tag = data.priority
            scheduleItemDetailsPriority.setMessage(data.priority.formattedStringRes)
        }
    }

    private class AuditoryInfoHeaderViewHolder(view: View) : BaseViewHolder<AuditoryInfoHeader>(view) {

        override fun onBind(data: AuditoryInfoHeader) {}
    }

    private class AuditoryInfoViewHolder(view: View) : BaseViewHolder<AuditoryInfo>(view) {

        override fun onBind(data: AuditoryInfo) {
            val auditory = data.auditory

            scheduleItemDetailsAuditoryName.text = with(auditory) { "$name-${building.name}" }
            scheduleItemDetailsAuditoryType.text = auditory.auditoryType.name

            scheduleItemDetailsAuditoryNote.isVisible = auditory.note.isNotEmpty()
            scheduleItemDetailsAuditoryNote.text = auditory.note

            val department = auditory.department
            scheduleItemDetailsAuditoryDepartment.isVisible = department != null
            scheduleItemDetailsAuditoryDepartment.text = department?.name
        }
    }

    private class NoteViewHolder(view: View) : BaseViewHolder<Note>(view) {

        override fun onBind(data: Note) {
            scheduleItemDetailsNoteText.apply {
                tag = data
                if (data.note != text?.toString()) {
                    setText(data.note)
                }
            }
        }
    }
}