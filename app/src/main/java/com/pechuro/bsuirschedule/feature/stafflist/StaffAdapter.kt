package com.pechuro.bsuirschedule.feature.stafflist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.Companion.TYPE_AUDITORY
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.Companion.TYPE_EMPLOYEE
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.Companion.TYPE_EMPTY
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.Companion.TYPE_GROUP
import kotlinx.android.synthetic.main.item_staff_auditory.*
import kotlinx.android.synthetic.main.item_staff_employee.*
import kotlinx.android.synthetic.main.item_staff_group.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StaffItemInformation>() {

    override fun areItemsTheSame(
            oldItem: StaffItemInformation,
            newItem: StaffItemInformation
    ) = oldItem === newItem

    override fun areContentsTheSame(
            oldItem: StaffItemInformation,
            newItem: StaffItemInformation
    ) = oldItem == newItem
}

class StaffAdapter(
        private val onItemClicked: (StaffItemInformation) -> Unit
) : ListAdapter<StaffItemInformation, BaseViewHolder<StaffItemInformation>>(DIFF_CALLBACK) {

    private val onClickListener: (View) -> Unit = {
        val info = it.tag as? StaffItemInformation
        info?.let { onItemClicked(info) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<StaffItemInformation> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_GROUP -> {
                val view = layoutInflater.inflate(R.layout.item_staff_group, parent, false)
                GroupViewHolder(view)
            }
            TYPE_EMPLOYEE -> {
                val view = layoutInflater.inflate(R.layout.item_staff_employee, parent, false)
                EmployeeViewHolder(view)
            }
            TYPE_EMPTY -> {
                val view = layoutInflater.inflate(R.layout.item_staff_empty, parent, false)
                EmptyViewHolder(view)
            }
            TYPE_AUDITORY -> {
                val view = layoutInflater.inflate(R.layout.item_staff_auditory, parent, false)
                AuditoryViewHolder(view)
            }
            else -> throw IllegalArgumentException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type

    override fun onBindViewHolder(holder: BaseViewHolder<StaffItemInformation>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int) = when (val item = getItem(position)) {
        is StaffItemInformation.GroupInfo -> item.group.id
        is StaffItemInformation.EmployeeInfo -> item.employee.id
        is StaffItemInformation.AuditoryInfo -> item.auditory.id
        else -> -1
    }

    private inner class GroupViewHolder(view: View) : BaseViewHolder<StaffItemInformation.GroupInfo>(view) {

        override fun onBind(data: StaffItemInformation.GroupInfo) {
            with(data.group) {
                suggestionGroupNumber.text = number
                suggestionGroupFacultyAbbreviation.text = faculty.abbreviation
                suggestionGroupSpecialityAbbreviation.text = speciality.abbreviation
                suggestionGroupEducationForm.text = speciality.educationForm.name
                suggestionGroupCourse.text = itemView.context.getString(
                        R.string.add_schedule_msg_suggestion_group_course,
                        course
                )
            }

            itemView.tag = data
            itemView.setSafeClickListener(onClick = onClickListener)
        }
    }

    private inner class EmployeeViewHolder(view: View) : BaseViewHolder<StaffItemInformation.EmployeeInfo>(view) {

        override fun onBind(data: StaffItemInformation.EmployeeInfo) {
            with(data.employee) {
                suggestionEmployeePhoto.loadPhoto(photoLink)
                suggestionEmployeeFirstName.text = firstName
                suggestionEmployeeMiddleName.text = middleName
                suggestionEmployeeLastName.text = lastName
                suggestionEmployeeDepartmentAbbreviation.text = itemView.context.getString(
                        R.string.add_schedule_msg_suggestion_employee_department,
                        department.abbreviation
                )
                suggestionEmployeeRank.text = rank
            }

            itemView.tag = data
            itemView.setSafeClickListener(onClick = onClickListener)
        }

        private fun ImageView.loadPhoto(url: String) {
            val glideOptions = RequestOptions().apply {
                placeholder(R.drawable.ic_photo_load_progress)
                error(R.drawable.ic_photo_load_error)
                circleCrop()
            }
            Glide.with(this)
                    .load(url)
                    .apply(glideOptions)
                    .into(this)
        }
    }

    private inner class AuditoryViewHolder(view: View) : BaseViewHolder<StaffItemInformation.AuditoryInfo>(view) {

        override fun onBind(data: StaffItemInformation.AuditoryInfo) {
            with(data.auditory) {
                staffAuditoryName.text = "$name-${building.name}"
                staffAuditoryType.text = auditoryType.name
                staffAuditoryCapacity.isVisible = capacity != 0 && capacity != -1
                staffAuditoryCapacity.text = itemView.context.getString(
                        R.string.add_schedule_msg_suggestion_auditory_capacity,
                        capacity
                )
                staffAuditoryDepartment.isVisible = department != null
                staffAuditoryDepartment.text = itemView.context.getString(
                        R.string.add_schedule_msg_suggestion_employee_department,
                        department?.abbreviation ?: ""
                )
                staffAuditoryNote.isVisible = note.isNotEmpty()
                staffAuditoryNote.text = note
            }
            itemView.tag = data
            itemView.setSafeClickListener(onClick = onClickListener)
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<StaffItemInformation>(view) {

        override fun onBind(data: StaffItemInformation) {}
    }
}