package com.pechuro.bsuirschedule.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.ui.fragment.list.ListAdapter
import com.pechuro.bsuirschedule.ui.fragment.list.ListItemData

object BindingUtils {

    @BindingAdapter("adapter")
    @JvmStatic
    fun setAdapter(recyclerView: RecyclerView, data: List<ListItemData>?) {
        val adapter = recyclerView.adapter as? ListAdapter
        adapter?.clearItems()
        data?.let { adapter?.addItems(it) }
    }

}