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
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.formattedString
import kotlinx.android.synthetic.main.item_lesson_details_header.*
import kotlinx.android.synthetic.main.item_lesson_details_location.*

private object DiffCallback : DiffUtil.ItemCallback<DetailsItem>() {

    override fun areItemsTheSame(oldItem: DetailsItem, newItem: DetailsItem): Boolean = if (oldItem.javaClass == newItem.javaClass) {
        oldItem.id == newItem.id
    } else {
        false
    }

    override fun areContentsTheSame(oldItem: DetailsItem, newItem: DetailsItem): Boolean {
        return oldItem == newItem
    }
}

class LessonDetailsAdapter : ListAdapter<DetailsItem, BaseViewHolder<DetailsItem>>(DiffCallback) {

    override fun getItemViewType(position: Int): Int = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DetailsItem> {
        return when (viewType) {
            DetailsItem.LESSON_HEADER_VIEW_TYPE -> HeaderViewHolder(R.layout.item_lesson_details_header.inflate(parent))
            DetailsItem.LOCATION_VIEW_TYPE -> LocationViewHolder(R.layout.item_lesson_details_location.inflate(parent))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DetailsItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    private fun Int.inflate(parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(this, parent, false)

    private class HeaderViewHolder(view: View) : BaseViewHolder<DetailsItem.LessonHeader>(view) {
        override fun onBind(data: DetailsItem.LessonHeader) {
            val lesson = data.lesson

            lessonDetailsTime.text = with(lesson) { "${startTime.formattedString} - ${endTime.formattedString}" }
            lessonDetailsWeeks.text = getWeeksText(itemView.context, data.weeks)

            lessonDetailsLocationsTitle.isVisible = lesson.auditories.isNotEmpty()

            return when (lesson) {
                is Lesson.GroupLesson -> onBindGroupHeader(lesson)
                is Lesson.EmployeeLesson -> onBindEmployeeHeader(lesson)
            }
        }

        private fun onBindEmployeeHeader(employeeLesson: Lesson.EmployeeLesson) {
            TODO("Not yet implemented")
        }

        private fun onBindGroupHeader(groupLesson: Lesson.GroupLesson) {
            lessonDetailsEmployeeView.employees = groupLesson.employees
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

    private class LocationViewHolder(view: View) : BaseViewHolder<DetailsItem.LocationItem>(view) {
        override fun onBind(data: DetailsItem.LocationItem) {
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
}