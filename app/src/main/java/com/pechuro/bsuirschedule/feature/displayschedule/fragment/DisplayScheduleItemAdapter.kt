package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import kotlinx.android.synthetic.main.item_display_student_classes.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleItem>() {

    override fun areItemsTheSame(
            oldItem: ScheduleItem,
            newItem: ScheduleItem
    ) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
            oldItem: ScheduleItem,
            newItem: ScheduleItem
    ) = oldItem == newItem
}

class DisplayScheduleItemAdapter : ListAdapter<ScheduleItem, BaseViewHolder<ScheduleItem>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ScheduleItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_display_student_classes, parent, false)
        return DayClassesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ScheduleItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position).id

    private inner class DayClassesViewHolder(view: View) : BaseViewHolder<ScheduleItem>(view) {

        override fun onBind(data: ScheduleItem) {
            displayStudentClassesTitle.text = data.subject
        }
    }
}