package com.pechuro.bsuirschedule.schedulewidget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo

class ScheduleWidgetViewService : RemoteViewsService() {

    companion object {

        const val EXTRA_SCHEDULE_INFO = "EXTRA_SCHEDULE_INFO"
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val widgetInfo = intent.getParcelableExtra<ScheduleWidgetInfo>(EXTRA_SCHEDULE_INFO)
                ?: throw IllegalArgumentException("Schedule info must be set")
        return ScheduleRemoteViewFactory(
                context = applicationContext,
                widgetInfo = widgetInfo
        )
    }
}

class ScheduleRemoteViewFactory(
        private val context: Context,
        private val widgetInfo: ScheduleWidgetInfo
) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {

    }

    override fun getLoadingView() = null

    override fun getItemId(position: Int) = position.toLong()

    override fun onDataSetChanged() {

    }

    override fun hasStableIds() = false

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_display_schedule_classes)
    }

    override fun getCount() = 0

    override fun getViewTypeCount() = 1

    override fun onDestroy() {

    }
}