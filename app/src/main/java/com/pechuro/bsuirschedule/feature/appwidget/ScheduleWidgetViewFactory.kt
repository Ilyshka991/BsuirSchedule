package com.pechuro.bsuirschedule.feature.appwidget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject

class ScheduleWidgetViewService : RemoteViewsService() {

    companion object {

        const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"
    }

    @Inject
    protected lateinit var dataProvider: ScheduleWidgetDataProvider

    override fun onCreate() {
        applicationContext.app.appComponent.inject(this)
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val widgetInfoId = intent.getIntExtra(EXTRA_WIDGET_ID, 0)
        return ScheduleWidgetRemoteViewFactory(
                context = applicationContext,
                widgetId = widgetInfoId,
                dataProvider = dataProvider
        )
    }
}

class ScheduleWidgetRemoteViewFactory(
        private val context: Context,
        private val widgetId: Int,
        private val dataProvider: ScheduleWidgetDataProvider
) : RemoteViewsService.RemoteViewsFactory {

    private var scheduleItems: List<ScheduleItem> = emptyList()

    override fun onCreate() {
        onDataSetChanged()
    }

    override fun onDataSetChanged() {
        scheduleItems = dataProvider.getScheduleItemsList(widgetId).scheduleItems
    }

    override fun getViewAt(position: Int): RemoteViews {
        val row = when (val scheduleItem = scheduleItems[position]) {
            is Lesson -> getLessonRow(scheduleItem)
            is Exam -> getExamRow(scheduleItem)
            else -> throw IllegalArgumentException("Not supported type: ${scheduleItem::class.java.name}")
        }
        row.setOnClickFillInIntent(R.id.scheduleWidgetItemParentView, Intent())
        return row
    }

    override fun getLoadingView() = null

    override fun getItemId(position: Int) = scheduleItems[position].id

    override fun hasStableIds() = true

    override fun getCount() = scheduleItems.size

    override fun getViewTypeCount() = 2

    override fun onDestroy() {}

    private fun getLessonRow(lesson: Lesson): RemoteViews {
        val row = RemoteViews(context.packageName, R.layout.item_widget_schedule_lesson)

        return row
    }

    private fun getExamRow(exam: Exam): RemoteViews {
        val row = RemoteViews(context.packageName, R.layout.item_widget_schedule_exam)

        return row
    }
}