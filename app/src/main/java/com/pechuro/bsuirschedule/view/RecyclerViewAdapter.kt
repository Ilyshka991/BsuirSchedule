package com.pechuro.bsuirschedule.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.repository.entity.Group
import kotlinx.android.synthetic.main.list_item.view.*

class RecyclerViewAdapter(private var items: List<Group>, private val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bindView(items[position])

    fun setItems(items: List<Group>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val mName = view.name

    fun bindView(group: Group) {
        mName.text = group.name
    }
}