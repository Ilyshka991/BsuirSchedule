package com.pechuro.bsuirschedule.schedulewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject

class ScheduleWidgetProvider : AppWidgetProvider() {

    companion object {

        fun updateWidget(
                context: Context,
                appWidgetManager: AppWidgetManager,
                widgetInfo: ScheduleWidgetInfo
        ) {
            appWidgetManager.updateAppWidget(widgetInfo.widgetId, null)
            val views = RemoteViews(context.packageName, R.layout.layout_schedule_widget)
            views.setTextViewText(R.id.scheduleWidgetTitle, widgetInfo.schedule.name)
            appWidgetManager.updateAppWidget(widgetInfo.widgetId, views)
        }
    }

    @Inject
    protected lateinit var widgetRepository: IWidgetRepository

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        injectDependencies(context)
        appWidgetIds.forEach { id ->
            val widgetInfo = widgetRepository.getScheduleWidget(id) ?: return@forEach
            updateWidget(
                    context = context,
                    appWidgetManager = appWidgetManager,
                    widgetInfo = widgetInfo
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.scheduleWidgetListView)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        injectDependencies(context)
        appWidgetIds.forEach { id ->
            widgetRepository.removeScheduleWidget(id)
        }
    }

    private fun injectDependencies(context: Context) {
        if (::widgetRepository.isInitialized) return
        context.app.appComponent.inject(this)
    }
}