package com.pechuro.bsuirschedule.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.app
import com.pechuro.bsuirschedule.feature.MainActivity
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationActivity
import javax.inject.Inject

class AppWidgetProvider : AppWidgetProvider() {

    companion object {

        fun updateWidget(
                context: Context,
                appWidgetManager: AppWidgetManager,
                widgetInfo: ScheduleWidgetInfo,
                widgetData: ScheduleWidgetData
        ) {
            val views = RemoteViews(context.packageName, R.layout.layout_schedule_widget)

            val dayStringRes = when {
                widgetInfo.schedule is Schedule.GroupExams -> R.string.schedule_widget_msg_exams
                widgetInfo.schedule is Schedule.EmployeeExams -> R.string.schedule_widget_msg_exams
                widgetData.isForTomorrow -> R.string.schedule_widget_msg_tomorrow
                else -> R.string.schedule_widget_msg_today
            }
            views.setTextViewText(R.id.scheduleWidgetDayText, context.getString(dayStringRes))
            views.setTextViewText(R.id.scheduleWidgetScheduleNameText, widgetInfo.schedule.name)

            val editWidgetInfoIntent = AppWidgetConfigurationActivity.newIntent(context).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetInfo.widgetId)
            }.run {
                PendingIntent.getActivity(context, widgetInfo.widgetId, this, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            views.setOnClickPendingIntent(R.id.scheduleWidgetTitleParentView, editWidgetInfoIntent)

            val listViewIntent = Intent(context, AppWidgetViewService::class.java).apply {
                putExtra(AppWidgetViewService.EXTRA_WIDGET_ID, widgetInfo.widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.scheduleWidgetListView, listViewIntent)
            views.setEmptyView(R.id.scheduleWidgetListView, R.id.scheduleWidgetListEmptyText)

            val openMainAppIntent = MainActivity.newIntent(context).apply {
                putExtra(MainActivity.EXTRA_SCHEDULE, widgetInfo.schedule)
            }.run {
                PendingIntent.getActivity(context, widgetInfo.widgetId, this, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            views.setPendingIntentTemplate(R.id.scheduleWidgetListView, openMainAppIntent)

            appWidgetManager.updateAppWidget(widgetInfo.widgetId, views)
        }
    }

    @Inject
    protected lateinit var widgetRepository: IWidgetRepository

    @Inject
    protected lateinit var dataProvider: AppWidgetDataProvider

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        injectDependencies(context)
        appWidgetIds.forEach { id ->
            val widgetInfo = widgetRepository.getScheduleWidget(id) ?: return@forEach
            updateWidget(
                    context = context,
                    appWidgetManager = appWidgetManager,
                    widgetInfo = widgetInfo,
                    widgetData = dataProvider.getScheduleItemsList(widgetInfo)
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