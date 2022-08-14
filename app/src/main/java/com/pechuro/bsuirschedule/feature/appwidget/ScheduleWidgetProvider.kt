package com.pechuro.bsuirschedule.feature.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Handler
import android.widget.RemoteViews
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo.WidgetTheme
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.app
import com.pechuro.bsuirschedule.feature.MainActivity
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationActivity
import javax.inject.Inject

class ScheduleWidgetProvider : AppWidgetProvider() {

    companion object {

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            widgetId: Int,
            widgetInfo: ScheduleWidgetInfo?,
            widgetData: ScheduleWidgetData
        ) {
            val layoutRes = when (widgetInfo?.theme) {
                WidgetTheme.LIGHT -> R.layout.layout_schedule_widget_light
                WidgetTheme.DARK -> R.layout.layout_schedule_widget_dark
                else -> {
                    when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_NO -> R.layout.layout_schedule_widget_light
                        Configuration.UI_MODE_NIGHT_YES -> R.layout.layout_schedule_widget_dark
                        else -> R.layout.layout_schedule_widget_light
                    }
                }
            }
            val views = RemoteViews(context.packageName, layoutRes)

            val dayStringRes = when {
                widgetInfo == null -> R.string.schedule_widget_msg_deleted
                widgetInfo.schedule is Schedule.GroupExams -> R.string.schedule_widget_msg_exams
                widgetInfo.schedule is Schedule.EmployeeExams -> R.string.schedule_widget_msg_exams
                widgetData.isForTomorrow -> R.string.schedule_widget_msg_tomorrow
                else -> R.string.schedule_widget_msg_today
            }
            views.setTextViewText(R.id.scheduleWidgetDayText, context.getString(dayStringRes))
            views.setTextViewText(
                R.id.scheduleWidgetScheduleNameText, widgetInfo?.schedule?.name
                    ?: ""
            )

            val editWidgetInfoIntent = AppWidgetConfigurationActivity.newIntent(context).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }.run {
                PendingIntent.getActivity(
                    context,
                    widgetId,
                    this,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            views.setOnClickPendingIntent(R.id.scheduleWidgetTitleParentView, editWidgetInfoIntent)

            val listViewIntent = Intent(context, ScheduleWidgetViewService::class.java).apply {
                putExtra(ScheduleWidgetViewService.EXTRA_WIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.scheduleWidgetListView, listViewIntent)
            views.setEmptyView(R.id.scheduleWidgetListView, R.id.scheduleWidgetListEmptyText)

            val openMainAppIntent = MainActivity.newIntent(context).apply {
                putExtra(MainActivity.EXTRA_SCHEDULE, widgetInfo?.schedule)
            }.run {
                PendingIntent.getActivity(
                    context,
                    widgetId,
                    this,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            views.setPendingIntentTemplate(R.id.scheduleWidgetListView, openMainAppIntent)

            appWidgetManager.updateAppWidget(widgetId, null)
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.scheduleWidgetListView)
            appWidgetManager.updateAppWidget(widgetId, views)

            //TODO: Figure out why it works
            Handler().postDelayed({
                appWidgetManager.notifyAppWidgetViewDataChanged(
                    widgetId,
                    R.id.scheduleWidgetListView
                )
                appWidgetManager.updateAppWidget(widgetId, views)
            }, 50)
        }
    }

    @Inject
    protected lateinit var widgetRepository: IWidgetRepository

    @Inject
    protected lateinit var dataProvider: ScheduleWidgetDataProvider

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        injectDependencies(context)
        appWidgetIds.forEach { id ->
            updateWidget(
                context = context,
                appWidgetManager = appWidgetManager,
                widgetId = id,
                widgetInfo = widgetRepository.getScheduleWidget(id),
                widgetData = dataProvider.getScheduleItemsList(id)
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.scheduleWidgetListView)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        injectDependencies(context)
        appWidgetIds.forEach { id ->
            val info = widgetRepository.getScheduleWidget(id)
            info?.let {
                AppAnalytics.report(AppAnalyticsEvent.Widget.Deleted(it.schedule))
            }
            widgetRepository.removeScheduleWidget(id)
        }
    }

    private fun injectDependencies(context: Context) {
        if (::widgetRepository.isInitialized) return
        context.app.appComponent.inject(this)
    }
}