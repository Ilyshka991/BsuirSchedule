package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.autoNotify
import kotlin.properties.Delegates

class NavigationDrawerAdapter : RecyclerView.Adapter<BaseViewHolder<NavigationDrawerItemInformation>>() {

    var scheduleList: List<NavigationDrawerItemInformation> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
                old, new,
                compareInstance = { o, n -> o === n },
                compareContent = { o, n -> o == n }
        )
    }

    var onScheduleClick: (Schedule) -> Unit = {}

    private val scheduleClickListener = View.OnClickListener {
        val schedule = it.tag as? Schedule ?: return@OnClickListener
        onScheduleClick(schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NavigationDrawerItemInformation> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NavigationDrawerItemInformation.ID_DIVIDER -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_drawer_divider, parent, false)
                DividerViewHolder(view)
            }
            NavigationDrawerItemInformation.ID_TITLE -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_drawer_title, parent, false)
                TitleViewHolder(view)
            }
            NavigationDrawerItemInformation.ID_CONTENT -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_drawer_content, parent, false)
                ContentViewHolder(view)
            }
            NavigationDrawerItemInformation.ID_EMPTY -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_drawer_empty, parent, false)
                EmptyViewHolder(view)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun getItemViewType(position: Int) = scheduleList[position].id

    override fun getItemCount() = scheduleList.size

    override fun onBindViewHolder(
            holder: BaseViewHolder<NavigationDrawerItemInformation>,
            position: Int
    ) = holder.onBind(scheduleList[position])

    private class DividerViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {}
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {}
    }

    private class TitleViewHolder(private val view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {
            if (data !is NavigationDrawerItemInformation.Title) return
            (view as? TextView)?.text = data.title
        }
    }

    private inner class ContentViewHolder(private val view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {
            if (data !is NavigationDrawerItemInformation.Content) return
            (view as? TextView)?.apply {
                text = data.schedule.name
                tag = data.schedule
                setOnClickListener(scheduleClickListener)
            }
        }
    }
}