package com.pechuro.bsuirschedule.feature.navigation.drawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerItemInformation.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NavigationDrawerItemInformation>() {

    override fun areItemsTheSame(
            oldItem: NavigationDrawerItemInformation,
            newItem: NavigationDrawerItemInformation
    ) = oldItem === newItem

    override fun areContentsTheSame(
            oldItem: NavigationDrawerItemInformation,
            newItem: NavigationDrawerItemInformation
    ) = when {
        oldItem is Empty && newItem is Empty -> true
        oldItem is Divider && newItem is Divider -> true
        oldItem is Title && newItem is Title -> newItem.scheduleType == oldItem.scheduleType
        oldItem is Content && newItem is Content -> newItem.schedule.name == oldItem.schedule.name
        else -> false
    }

}

class NavigationDrawerAdapter : ListAdapter<NavigationDrawerItemInformation, BaseViewHolder<NavigationDrawerItemInformation>>(DIFF_CALLBACK) {

    interface ActionCallback {

        fun onScheduleClicked(schedule: Schedule)

        fun onScheduleLongClicked(schedule: Schedule)

        fun onTitleClicked(scheduleType: ScheduleType)

        fun onTitleLongClicked(scheduleType: ScheduleType)
    }

    var actionCallback: ActionCallback? = null

    private val scheduleClickListener = View.OnClickListener {
        val schedule = it.tag as? Schedule ?: return@OnClickListener
        actionCallback?.onScheduleClicked(schedule)
    }
    private val scheduleLongClickListener = View.OnLongClickListener {
        val schedule = it.tag as? Schedule ?: return@OnLongClickListener false
        actionCallback?.onScheduleLongClicked(schedule)
        true
    }

    private val titleClickListener = View.OnClickListener {
        val scheduleType = it.tag as? ScheduleType ?: return@OnClickListener
        actionCallback?.onTitleClicked(scheduleType)
    }
    private val titleLongClickListener = View.OnLongClickListener {
        val scheduleType = it.tag as? ScheduleType ?: return@OnLongClickListener false
        actionCallback?.onTitleLongClicked(scheduleType)
        true
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
            else -> throw IllegalStateException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).id

    override fun onBindViewHolder(
            holder: BaseViewHolder<NavigationDrawerItemInformation>,
            position: Int
    ) = holder.onBind(getItem(position))

    private class DividerViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {}
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {}
    }

    private inner class TitleViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {
            if (data !is Title) return
            val titleRes = when (data.scheduleType) {
                ScheduleType.CLASSES -> R.string.msg_classes
                ScheduleType.EXAMS -> R.string.msg_exams
            }
            (containerView as TextView).apply {
                setText(titleRes)
                setOnClickListener(titleClickListener)
                setOnLongClickListener(titleLongClickListener)
            }
        }
    }

    private inner class ContentViewHolder(view: View) : BaseViewHolder<NavigationDrawerItemInformation>(view) {

        override fun onBind(data: NavigationDrawerItemInformation) {
            if (data !is Content) return
            (containerView as TextView).apply {
                text = data.schedule.name
                tag = data.schedule
                setOnClickListener(scheduleClickListener)
                setOnLongClickListener(scheduleLongClickListener)
            }
        }
    }
}