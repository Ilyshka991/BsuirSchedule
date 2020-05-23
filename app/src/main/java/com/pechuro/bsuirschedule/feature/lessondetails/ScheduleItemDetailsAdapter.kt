package com.pechuro.bsuirschedule.feature.lessondetails

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
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.*
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_AUDITORY_HEADER
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_AUDITORY_INFO
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_EMPLOYEE_INFO
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_NOTE
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_TIME
import com.pechuro.bsuirschedule.feature.lessondetails.ScheduleItemDetailsInfo.Companion.VIEW_TYPE_WEEKS
import kotlinx.android.synthetic.main.item_lesson_details_employees.*
import kotlinx.android.synthetic.main.item_lesson_details_footer.*
import kotlinx.android.synthetic.main.item_lesson_details_location.*
import kotlinx.android.synthetic.main.item_lesson_details_time.*
import kotlinx.android.synthetic.main.item_lesson_details_weeks.*

private object DiffCallback : DiffUtil.ItemCallback<ScheduleItemDetailsInfo>() {

    override fun areItemsTheSame(old: ScheduleItemDetailsInfo, new: ScheduleItemDetailsInfo): Boolean = when {
        old is AuditoryInfo && new is AuditoryInfo -> old.auditory.id == new.auditory.id
        else -> old === new
    }

    override fun areContentsTheSame(oldItem: ScheduleItemDetailsInfo, newItem: ScheduleItemDetailsInfo): Boolean {
        return oldItem == newItem
    }
}

class ScheduleItemDetailsAdapter : ListAdapter<ScheduleItemDetailsInfo, BaseViewHolder<ScheduleItemDetailsInfo>>(DiffCallback) {

    override fun getItemViewType(position: Int): Int = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ScheduleItemDetailsInfo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TIME -> {
                val view = layoutInflater.inflate(R.layout.item_lesson_details_time, parent, false)
                TimeViewHolder(view)
            }
            VIEW_TYPE_EMPLOYEE_INFO -> {
                val view=layoutInflater.inflate(R.layout.item_lesson_details_employees, parent, false)
                EmployeeInfoViewHolder(view)
            }
            VIEW_TYPE_WEEKS -> {
                val view=layoutInflater.inflate(R.layout.item_lesson_details_weeks, parent, false)
                WeeksViewHolder(view)
            }
            VIEW_TYPE_AUDITORY_HEADER -> {
                val view=layoutInflater.inflate(R.layout.item_lesson_details_location_header, parent, false)
                LocationHeaderViewHolder(view)
            }
            VIEW_TYPE_AUDITORY_INFO -> {
                val view=layoutInflater.inflate(R.layout.item_lesson_details_location, parent, false)
                LocationItemViewHolder(view)
            }
            VIEW_TYPE_NOTE -> {
                val view=layoutInflater.inflate(R.layout.item_lesson_details_footer, parent, false)
                LessonFooterViewHolder(view)
            }
            else -> throw IllegalArgumentException("Not supported type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ScheduleItemDetailsInfo>, position: Int) {
        holder.onBind(getItem(position))
    }

    private class TimeViewHolder(view: View) : BaseViewHolder<Time>(view) {

        override fun onBind(data: Time) {
            lessonDetailsTime.text = with(data) { "${startTime.formattedString} - ${endTime.formattedString}" }
        }
    }

    private class EmployeeInfoViewHolder(view: View) : BaseViewHolder<EmployeeInfo>(view) {

        override fun onBind(data: EmployeeInfo) {
            lessonDetailsEmployeeView.employees = data.employees
        }
    }

    private class WeeksViewHolder(view: View) : BaseViewHolder<Weeks>(view) {

        override fun onBind(data: Weeks) {
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

    private class LocationHeaderViewHolder(view: View) : BaseViewHolder<AuditoryHeader>(view) {
        override fun onBind(data: AuditoryHeader) {}
    }

    private class LocationItemViewHolder(view: View) : BaseViewHolder<AuditoryInfo>(view) {

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

    private class LessonFooterViewHolder(view: View) : BaseViewHolder<Note>(view) {

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