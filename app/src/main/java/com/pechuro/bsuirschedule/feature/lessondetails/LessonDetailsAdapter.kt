package com.pechuro.bsuirschedule.feature.lessondetails

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Lesson
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_employee_details.*
import kotlinx.android.synthetic.main.item_lesson_details_header.*
import kotlinx.android.synthetic.main.item_lesson_details_location.*

class LessonDetailsAdapter(
        private val lesson: Lesson
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER_VIEW_TYPE = R.layout.item_lesson_details_header
        private const val LOCATION_VIEW_TYPE = R.layout.item_lesson_details_location
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> HEADER_VIEW_TYPE
        else -> LOCATION_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(view).also { initHeader(it) }
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
        lessonDetailsTime.text = with(lesson) { "$startTime - $endTime" }
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
        holder.lessonDetailsInfoViewPager.visibility =
                if (groupLesson.employees.isNotEmpty()) View.VISIBLE
                else View.GONE
        holder.lessonDetailsInfoTabLayout.visibility =
                if (groupLesson.employees.size > 1) View.VISIBLE
                else View.GONE
    }

    private fun getWeeksText(context: Context): CharSequence {
        val builder = SpannableStringBuilder()
        builder.bold {
            val prefix = context.getString(R.string.lesson_details_weeks)
            append(prefix)
        }
        builder.append(" ${lesson.weekDay.index + 1}")
        return builder.toSpannable()
    }

    private fun initHeader(holder: HeaderViewHolder) = when (lesson) {
        is Lesson.GroupLesson -> initGroupHeader(holder, lesson)
        is Lesson.EmployeeLesson -> initEmployeeHeader(holder, lesson)
    }

    private fun initEmployeeHeader(holder: HeaderViewHolder, employeeLesson: Lesson.EmployeeLesson) {
        TODO("Not yet implemented")
    }

    private fun initGroupHeader(holder: HeaderViewHolder, groupLesson: Lesson.GroupLesson) {
        holder.lessonDetailsInfoViewPager.adapter = EmployeeAdapter(groupLesson)
        TabLayoutMediator(holder.lessonDetailsInfoTabLayout, holder.lessonDetailsInfoViewPager) { _, _ -> }.attach()
    }

    private class HeaderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    private class LocationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    private class EmployeeAdapter(
            private val groupLesson: Lesson.GroupLesson
    ) : RecyclerView.Adapter<EmployeeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee_details, parent, false)
            return EmployeeViewHolder(view)
        }

        override fun getItemCount(): Int = groupLesson.employees.size

        override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
            val employee = groupLesson.employees[position]
            holder.employeeDetailsFullName.text = with(employee) {
                "$firstName $middleName $lastName"
            }
            Glide.with(holder.itemView)
                    .load(employee.photoLink)
                    .placeholder(R.drawable.employee_placeholder)
                    .error(R.drawable.employee_placeholder)
                    .circleCrop()
                    .into(holder.employeeDetailsPhoto)
        }
    }

    private class EmployeeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}