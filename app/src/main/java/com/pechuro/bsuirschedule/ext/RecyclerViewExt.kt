package com.pechuro.bsuirschedule.ext

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T> RecyclerView.Adapter<*>.autoNotify(
        old: List<T>, new: List<T>,
        compareInstance: (T, T) -> Boolean,
        compareContent: (T, T) -> Boolean) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compareInstance(old[oldItemPosition], new[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compareContent(old[oldItemPosition], new[newItemPosition])
        }

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size
    })

    diff.dispatchUpdatesTo(this)
}