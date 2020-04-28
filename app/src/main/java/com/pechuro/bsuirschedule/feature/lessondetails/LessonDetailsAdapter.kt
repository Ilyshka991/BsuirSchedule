package com.pechuro.bsuirschedule.feature.lessondetails

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.formattedString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_lesson_details_header.*
import kotlinx.android.synthetic.main.item_lesson_details_location.*

class LessonDetailsAdapter(
        private val lesson: Lesson
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER_VIEW_TYPE = R.layout.item_lesson_details_header
        private const val LOCATION_VIEW_TYPE = R.layout.item_lesson_details_location
    }

    var weeks: List<WeekNumber> = emptyList()
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> HEADER_VIEW_TYPE
        else -> LOCATION_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(view)
            LOCATION_VIEW_TYPE -> LocationViewHolder(view)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = 1 + lesson.auditories.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> onBindHeader(holder)
            is LocationViewHolder -> onBindLocation(holder, lesson.auditories[position - 1])
        }
    }

    private fun onBindLocation(holder: LocationViewHolder, auditory: Auditory) = with(holder) {
        lessonDetailsLocationAuditoryName.text = with(auditory) {
            "$name-${building.name}"
        }
        lessonDetailsLocationAuditoryType.text = auditory.auditoryType.name

        lessonDetailsLocationAuditoryNote.visibility =
                if (auditory.note.isNotEmpty()) {
                    lessonDetailsLocationAuditoryNote.text = auditory.note
                    View.VISIBLE
                } else {
                    View.GONE
                }

        val department = auditory.department
        lessonDetailsLocationAuditoryDepartment.visibility =
                if (department != null) {
                    lessonDetailsLocationAuditoryDepartment.text = department.name
                    View.VISIBLE
                } else {
                    View.GONE
                }
    }

    private fun onBindHeader(holder: HeaderViewHolder) = with(holder) {
        val context = itemView.context

        lessonDetailsName.text = lesson.subject
        lessonDetailsTime.text = with(lesson) { "${startTime.formattedString} - ${endTime.formattedString}" }
        lessonDetailsWeeks.text = getWeeksText(context)

        lessonDetailsLocationsTitle.visibility =
                if (lesson.auditories.isNotEmpty()) View.VISIBLE
                else View.GONE

        return@with when (lesson) {
            is Lesson.GroupLesson -> onBindGroupHeader(holder, lesson)
            is Lesson.EmployeeLesson -> onBindEmployeeHeader(holder, lesson)
        }
    }

    private fun onBindEmployeeHeader(holder: HeaderViewHolder, employeeLesson: Lesson.EmployeeLesson) {
        TODO()
    }

    private fun onBindGroupHeader(holder: HeaderViewHolder, groupLesson: Lesson.GroupLesson) {
        holder.lessonDetailsEmployeeView.employees = groupLesson.employees
    }

    private fun getWeeksText(context: Context): CharSequence {
        val builder = SpannableStringBuilder()
        builder.bold {
            val prefix = context.getString(R.string.lesson_details_weeks)
            append(prefix)
        }
        builder.append(" ")
        builder.append(weeks.joinToString { it.formattedString })
        return builder.toSpannable()
    }

    private class HeaderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    private class LocationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}