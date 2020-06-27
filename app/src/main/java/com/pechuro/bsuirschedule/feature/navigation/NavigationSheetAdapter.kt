package com.pechuro.bsuirschedule.feature.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.*
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState.*
import kotlinx.android.synthetic.main.item_navigation_sheet_content.*
import kotlinx.android.synthetic.main.item_navigation_sheet_empty.*
import kotlinx.android.synthetic.main.item_navigation_sheet_hint.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NavigationSheetItemInformation>() {

    override fun areItemsTheSame(
            oldItem: NavigationSheetItemInformation,
            newItem: NavigationSheetItemInformation
    ) = oldItem == newItem

    override fun areContentsTheSame(
            oldItem: NavigationSheetItemInformation,
            newItem: NavigationSheetItemInformation
    ) = oldItem == newItem
}

class NavigationDrawerAdapter : ListAdapter<NavigationSheetItemInformation, BaseViewHolder<NavigationSheetItemInformation>>(DIFF_CALLBACK) {

    interface ActionCallback {

        fun onScheduleClicked(schedule: Schedule)

        fun onHintDismissed()
    }

    companion object {

        private const val EMPTY_VIEW_DISPLAY_DELAY_MS = 1000L
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
            NavigationSheetItemInformation.ID_HINT -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_hint, parent, false)
                HintViewHolder(view)
            }
            else -> throw IllegalStateException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).id

    override fun onBindViewHolder(
            holder: BaseViewHolder<NavigationSheetItemInformation>,
            position: Int
    ) = holder.onBind(getItem(position))

    override fun getItemId(position: Int) = getItem(position).hashCode().toLong()

    fun getItemAt(position: Int): NavigationSheetItemInformation = getItem(position)

    private class DividerViewHolder(view: View) : BaseViewHolder<Divider>(view) {

        override fun onBind(data: Divider) {}
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<Empty>(view) {

        override fun onBind(data: Empty) {
            navigationItemEmptyParentView.isVisible = false
            itemView.postDelayed({
                navigationItemEmptyParentView.isVisible = true
            }, EMPTY_VIEW_DISPLAY_DELAY_MS)
        }
    }

    private class TitleViewHolder(view: View) : BaseViewHolder<Title>(view) {

        override fun onBind(data: Title) {
            val titleRes = when (data.scheduleType) {
                ScheduleType.CLASSES -> R.string.navigation_title_classes
                ScheduleType.EXAMS -> R.string.navigation_title_exams
            }
            (containerView as? TextView)?.setText(titleRes)
        }
    }

    private inner class ContentViewHolder(view: View) : BaseViewHolder<Content>(view) {

        override fun onBind(data: Content) {
            navigationItemContentText.apply {
                text = data.schedule.name
                isSelected = data.isSelected
                setSafeClickListener(onClickListener = scheduleClickListener)
                tag = data.schedule
            }
            navigationItemContentUpdateParentView.isVisible = data.updateState != NOT_AVAILABLE
            navigationItemContentUpdateLoaderView.isVisible = data.updateState == IN_PROGRESS
            navigationItemContentUpdateSuccessText.isVisible = data.updateState == SUCCESS
            navigationItemContentUpdateErrorText.isVisible = data.updateState == ERROR
            navigationItemContentUpdateAvailableText.isVisible = data.updateState == AVAILABLE
            isSwipeAllowed = data.updateState == NOT_AVAILABLE || data.updateState == AVAILABLE
        }
    }

    private inner class HintViewHolder(view: View) : BaseViewHolder<Hint>(view) {

        init {
            navigationItemHintOkButton.setSafeClickListener {
                actionCallback?.onHintDismissed()
            }
        }

        override fun onBind(data: Hint) {}
    }
}