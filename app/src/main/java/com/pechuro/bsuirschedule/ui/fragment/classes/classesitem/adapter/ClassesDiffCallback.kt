package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData


class ClassesDiffCallback : DiffUtil.Callback() {

    private var oldList: List<BaseClassesData> = emptyList()
    private var newList: List<BaseClassesData> = emptyList()

    fun setData(oldList: List<BaseClassesData>, newList: List<BaseClassesData>) {
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