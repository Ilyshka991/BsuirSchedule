package com.pechuro.bsuirschedule.feature.lessondetails

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.item_lesson_details_employees.*
import kotlinx.android.synthetic.main.item_lesson_details_footer.*
import kotlinx.android.synthetic.main.item_lesson_details_location.*
import kotlinx.android.synthetic.main.item_lesson_details_time.*
import kotlinx.android.synthetic.main.item_lesson_details_weeks.*

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
            DetailsItem.TIME_VIEW_TYPE -> TimeViewHolder(R.layout.item_lesson_details_time.inflate(parent))
            DetailsItem.EMPLOYEE_INFO_VIEW_TYPE -> EmployeeInfoViewHolder(R.layout.item_lesson_details_employees.inflate(parent))
            DetailsItem.WEEKS_VIEW_TYPE -> WeeksViewHolder(R.layout.item_lesson_details_weeks.inflate(parent))
            DetailsItem.LOCATION_HEADER_VIEW_TYPE -> LocationHeaderViewHolder(R.layout.item_lesson_details_location_header.inflate(parent))
            DetailsItem.LOCATION_ITEM_VIEW_TYPE -> LocationItemViewHolder(R.layout.item_lesson_details_location.inflate(parent))
            DetailsItem.LESSON_FOOTER_VIEW_TYPE -> LessonFooterViewHolder(R.layout.item_lesson_details_footer.inflate(parent))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DetailsItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    private fun Int.inflate(parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(this, parent, false)

    private class TimeViewHolder(view: View) : BaseViewHolder<DetailsItem.Time>(view) {
        override fun onBind(data: DetailsItem.Time) {
            lessonDetailsTime.text = with(data) { "${startTime.formattedString} - ${endTime.formattedString}" }
        }
    }

    private class EmployeeInfoViewHolder(view: View) : BaseViewHolder<DetailsItem.EmployeeInfo>(view) {
        override fun onBind(data: DetailsItem.EmployeeInfo) {
            lessonDetailsEmployeeView.employees = data.employees
        }
    }

    private class WeeksViewHolder(view: View) : BaseViewHolder<DetailsItem.Weeks>(view) {
        override fun onBind(data: DetailsItem.Weeks) {
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

    private class LocationHeaderViewHolder(view: View) : BaseViewHolder<DetailsItem.LocationHeader>(view) {
        override fun onBind(data: DetailsItem.LocationHeader) {}
    }

    private class LocationItemViewHolder(view: View) : BaseViewHolder<DetailsItem.LocationItem>(view) {
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

    private class LessonFooterViewHolder(view: View) : BaseViewHolder<DetailsItem.LessonFooter>(view) {

        init {
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val data = lessonDetailsNoteText.tag as? DetailsItem.LessonFooter ?: return
                    data.onNoteChanged(s.toString())
                }
            }
            lessonDetailsNoteText.addTextChangedListener(textWatcher)
        }

        override fun onBind(data: DetailsItem.LessonFooter) {
            lessonDetailsNoteText.apply {
                tag = data
                if (data.note != text?.toString()) {
                    setText(data.note)
                }
            }
        }
    }
}