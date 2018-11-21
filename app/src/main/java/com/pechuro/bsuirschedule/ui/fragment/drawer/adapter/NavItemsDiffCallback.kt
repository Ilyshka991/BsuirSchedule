package com.pechuro.bsuirschedule.ui.fragment.drawer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation


class NavItemsDiffCallback : DiffUtil.Callback() {

    private var oldList: List<ScheduleInformation> = emptyList()
    private var newList: List<ScheduleInformation> = emptyList()

    fun setData(oldList: List<ScheduleInformation>, newList: List<ScheduleInformation>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
}