package com.pechuro.bsuirschedule.appwidget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject

class AppWidgetViewService : RemoteViewsService() {

    companion object {

        const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"
    }

    @Inject
    protected lateinit var dataProvider: AppWidgetDataProvider

    @Inject
    protected lateinit var widgetRepository: IWidgetRepository

    override fun onCreate() {
        applicationContext.app.appComponent.inject(this)
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val widgetInfoId = intent.getIntExtra(EXTRA_WIDGET_ID, 0)
        val widgetInfo = widgetRepository.getScheduleWidget(widgetInfoId)
                ?: throw IllegalArgumentException("Widget info must not be null")
        return AppWidgetRemoteViewFactory(
                context = applicationContext,
                widgetInfo = widgetInfo,
                dataProvider = dataProvider
        )
    }
}

class AppWidgetRemoteViewFactory(
        private val context: Context,
        private val widgetInfo: ScheduleWidgetInfo,
        private val dataProvider: AppWidgetDataProvider
) : RemoteViewsService.RemoteViewsFactory {

    private var scheduleItems: List<ScheduleItem> = emptyList()

    override fun onCreate() {
        onDataSetChanged()
    }

    override fun onDataSetChanged() {
        scheduleItems = dataProvider.getScheduleItemsList(widgetInfo).scheduleItems
    }

    override fun getViewAt(position: Int): RemoteViews {
        val row = RemoteViews(context.packageName, R.layout.item_schedule_widget)
        row.setOnClickFillInIntent(R.id.scheduleWidgetItemParentView, Intent())
        return row
    }

    override fun getLoadingView() = null

    override fun getItemId(position: Int) = scheduleItems[position].id

    override fun hasStableIds() = true

    override fun getCount() = scheduleItems.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {}
}