package com.pechuro.bsuirschedule.feature.addschedule.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.feature.addschedule.fragment.SuggestionItemInformation.Companion.TYPE_EMPLOYEE
import com.pechuro.bsuirschedule.feature.addschedule.fragment.SuggestionItemInformation.Companion.TYPE_GROUP
import kotlinx.android.synthetic.main.item_suggestion_employee.*
import kotlinx.android.synthetic.main.item_suggestion_group.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SuggestionItemInformation>() {

    override fun areItemsTheSame(
            oldItem: SuggestionItemInformation,
            newItem: SuggestionItemInformation
    ) = oldItem === newItem

    override fun areContentsTheSame(
            oldItem: SuggestionItemInformation,
            newItem: SuggestionItemInformation
    ) = when {
        oldItem is SuggestionItemInformation.EmployeeInfo && newItem is SuggestionItemInformation.EmployeeInfo -> {
            oldItem.employee == newItem.employee
        }
        oldItem is SuggestionItemInformation.GroupInfo && newItem is SuggestionItemInformation.GroupInfo -> {
            oldItem.group == newItem.group
        }
        else -> false
    }
}

class AddScheduleSuggestionsAdapter : ListAdapter<SuggestionItemInformation, BaseViewHolder<SuggestionItemInformation>>(DIFF_CALLBACK) {

    var onItemClicked: (SuggestionItemInformation) -> Unit = {}

    private val onClickListener = View.OnClickListener {
        val info = it.tag as? SuggestionItemInformation
                ?: return@OnClickListener
        onItemClicked(info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SuggestionItemInformation> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_GROUP -> {
                val view = layoutInflater.inflate(R.layout.item_suggestion_group, parent, false)
                GroupViewHolder(view)
            }
            TYPE_EMPLOYEE -> {
                val view = layoutInflater.inflate(R.layout.item_suggestion_employee, parent, false)
                EmployeeViewHolder(view)
            }
            else -> throw IllegalArgumentException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type

    override fun onBindViewHolder(holder: BaseViewHolder<SuggestionItemInformation>, position: Int) {
        holder.onBind(getItem(position))
    }

    private inner class GroupViewHolder(view: View) : BaseViewHolder<SuggestionItemInformation>(view) {

        override fun onBind(data: SuggestionItemInformation) {
            if (data !is SuggestionItemInformation.GroupInfo) return

            with(data.group) {
                suggestionGroupNumber.text = number
                suggestionGroupFacultyAbbreviation.text = faculty.abbreviation
                suggestionGroupSpecialityAbbreviation.text = speciality.abbreviation
                suggestionGroupEducationForm.text = speciality.educationForm.name
                suggestionGroupCourse.text = course.toString()
            }

            itemView.tag = data
            itemView.setOnClickListener(onClickListener)
        }
    }

    private inner class EmployeeViewHolder(view: View) : BaseViewHolder<SuggestionItemInformation>(view) {

        override fun onBind(data: SuggestionItemInformation) {
            if (data !is SuggestionItemInformation.EmployeeInfo) return

            with(data.employee) {
                suggestionEmployeePhoto.loadPhoto(photoLink)
                suggestionEmployeeFirstName.text = firstName
                suggestionEmployeeMiddleName.text = middleName
                suggestionEmployeeLastName.text = lastName
                suggestionEmployeeDepartmentAbbreviation.text = department.abbreviation
                suggestionEmployeeRank.text = rank
            }

            itemView.tag = data
            itemView.setOnClickListener(onClickListener)
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
}