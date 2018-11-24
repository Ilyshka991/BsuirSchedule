package com.pechuro.bsuirschedule.ui.fragment.exam.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.bsuirschedule.ui.fragment.exam.data.BaseExamData


class ExamsDiffCallback : DiffUtil.Callback() {

    private var oldList: List<BaseExamData> = emptyList()
    private var newList: List<BaseExamData> = emptyList()

    fun setData(oldList: List<BaseExamData>, newList: List<BaseExamData>) {
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