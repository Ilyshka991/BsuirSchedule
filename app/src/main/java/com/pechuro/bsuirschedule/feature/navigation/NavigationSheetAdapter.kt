package com.pechuro.bsuirschedule.feature.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.*
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState.*
import kotlinx.android.synthetic.main.item_navigation_sheet_content.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NavigationSheetItemInformation>() {

    override fun areItemsTheSame(
            oldItem: NavigationSheetItemInformation,
            newItem: NavigationSheetItemInformation
    ) = oldItem === newItem

    override fun areContentsTheSame(
            oldItem: NavigationSheetItemInformation,
            newItem: NavigationSheetItemInformation
    ) = when {
        oldItem is Empty && newItem is Empty -> true
        oldItem is Divider && newItem is Divider -> true
        oldItem is Title && newItem is Title -> newItem.scheduleType == oldItem.scheduleType
        oldItem is Content && newItem is Content -> newItem.schedule.name == oldItem.schedule.name
        else -> false
    }

}

class NavigationDrawerAdapter : ListAdapter<NavigationSheetItemInformation, BaseViewHolder<NavigationSheetItemInformation>>(DIFF_CALLBACK) {

    interface ActionCallback {

        fun onScheduleClicked(schedule: Schedule)
    }

    var actionCallback: ActionCallback? = null

    private val scheduleClickListener = View.OnClickListener {
        val schedule = it.tag as? Schedule ?: return@OnClickListener
        actionCallback?.onScheduleClicked(schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NavigationSheetItemInformation> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NavigationSheetItemInformation.ID_DIVIDER -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_divider, parent, false)
                DividerViewHolder(view)
            }
            NavigationSheetItemInformation.ID_TITLE -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_title, parent, false)
                TitleViewHolder(view)
            }
            NavigationSheetItemInformation.ID_CONTENT -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_content, parent, false)
                ContentViewHolder(view)
            }
            NavigationSheetItemInformation.ID_EMPTY -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_empty, parent, false)
                EmptyViewHolder(view)
            }
            else -> throw IllegalStateException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).id

    override fun onBindViewHolder(
            holder: BaseViewHolder<NavigationSheetItemInformation>,
            position: Int
    ) = holder.onBind(getItem(position))

    fun getItemAt(position: Int): NavigationSheetItemInformation = getItem(position)

    private class DividerViewHolder(view: View) : BaseViewHolder<NavigationSheetItemInformation>(view) {

        override fun onBind(data: NavigationSheetItemInformation) {}
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<NavigationSheetItemInformation>(view) {

        override fun onBind(data: NavigationSheetItemInformation) {}
    }

    private inner class TitleViewHolder(view: View) : BaseViewHolder<NavigationSheetItemInformation>(view) {

        override fun onBind(data: NavigationSheetItemInformation) {
            if (data !is Title) return
            val titleRes = when (data.scheduleType) {
                ScheduleType.CLASSES -> R.string.navigation_title_classes
                ScheduleType.EXAMS -> R.string.navigation_title_exams
            }
            (containerView as? TextView)?.setText(titleRes)
        }
    }

    private inner class ContentViewHolder(view: View) : BaseViewHolder<NavigationSheetItemInformation>(view) {

        override fun onBind(data: NavigationSheetItemInformation) {
            if (data !is Content) return
            navigationItemContentText.apply {
                tag = data.schedule
                text = data.schedule.name
                setOnClickListener(scheduleClickListener)
            }
            navigationItemContentUpdateParentView.isVisible = data.updateState != NOT_AVAILABLE
            navigationItemContentUpdateLoaderView.isVisible = data.updateState == IN_PROGRESS
            navigationItemContentUpdateSuccessText.isVisible = data.updateState == SUCCESS
            navigationItemContentUpdateErrorText.isVisible = data.updateState == ERROR
            navigationItemContentUpdateAvailableText.isVisible = data.updateState == AVAILABLE
            isSwipeAllowed = data.updateState == NOT_AVAILABLE || data.updateState == AVAILABLE
        }
    }
}