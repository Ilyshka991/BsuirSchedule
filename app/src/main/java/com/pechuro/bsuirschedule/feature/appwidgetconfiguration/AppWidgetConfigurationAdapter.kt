package com.pechuro.bsuirschedule.feature.appwidgetconfiguration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationScheduleDisplayData.Content
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationScheduleDisplayData.Title
import kotlinx.android.synthetic.main.item_navigation_sheet_content.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppWidgetConfigurationScheduleDisplayData>() {

    override fun areItemsTheSame(
            oldItem: AppWidgetConfigurationScheduleDisplayData,
            newItem: AppWidgetConfigurationScheduleDisplayData
    ) = when {
        oldItem is Content && newItem is Content -> oldItem.schedule == newItem.schedule
        else -> oldItem == newItem
    }

    override fun areContentsTheSame(
            oldItem: AppWidgetConfigurationScheduleDisplayData,
            newItem: AppWidgetConfigurationScheduleDisplayData
    ) = oldItem == newItem
}

private const val VIEW_TYPE_CONTENT = 101
private const val VIEW_TYPE_TITLE = 102

class AppWidgetConfigurationAdapter(
        private val onScheduleClicked: (Schedule) -> Unit
) : ListAdapter<AppWidgetConfigurationScheduleDisplayData, BaseViewHolder<AppWidgetConfigurationScheduleDisplayData>>(DIFF_CALLBACK) {

    private val scheduleClickListener = View.OnClickListener {
        val schedule = it.tag as? Schedule ?: return@OnClickListener
        onScheduleClicked(schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AppWidgetConfigurationScheduleDisplayData> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_CONTENT -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_content, parent, false)
                ContentViewHolder(view)
            }
            VIEW_TYPE_TITLE -> {
                val view = layoutInflater.inflate(R.layout.item_navigation_sheet_title, parent, false)
                TitleViewHolder(view)
            }
            else -> throw IllegalStateException("Not supported type: $viewType")
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is Content -> VIEW_TYPE_CONTENT
        is Title -> VIEW_TYPE_TITLE
    }

    override fun onBindViewHolder(
            holder: BaseViewHolder<AppWidgetConfigurationScheduleDisplayData>,
            position: Int
    ) = holder.onBind(getItem(position))

    override fun getItemId(position: Int) = getItem(position).hashCode().toLong()

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
                isSelected = data.checked
                setSafeClickListener(onClickListener = scheduleClickListener)
                tag = data.schedule
            }
        }
    }
}