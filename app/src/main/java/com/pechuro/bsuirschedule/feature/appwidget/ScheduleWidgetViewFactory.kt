package com.pechuro.bsuirschedule.feature.appwidget

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.graphics.drawable.toBitmap
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.display.fragment.formatAuditories
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
    private var widgetInfo: ScheduleWidgetInfo? = null

    override fun onCreate() {
        onDataSetChanged()
    }

    override fun onDataSetChanged() {
        scheduleItems = dataProvider.getScheduleItemsList(widgetId).scheduleItems
        widgetInfo = dataProvider.getWidgetInfo(widgetId)
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

    override fun hasStableIds() = false

    override fun getCount() = scheduleItems.size

    override fun getViewTypeCount() = 4

    override fun onDestroy() {}

    private fun getLessonRow(lesson: Lesson): RemoteViews {
        val layoutRes = when (widgetInfo?.theme) {
            ScheduleWidgetInfo.WidgetTheme.LIGHT -> R.layout.item_widget_schedule_lesson_light
            ScheduleWidgetInfo.WidgetTheme.DARK -> R.layout.item_widget_schedule_lesson_dark
            else -> {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> R.layout.item_widget_schedule_lesson_light
                    Configuration.UI_MODE_NIGHT_YES -> R.layout.item_widget_schedule_lesson_dark
                    else -> R.layout.item_widget_schedule_lesson_light
                }
            }
        }
        val row = RemoteViews(context.packageName, layoutRes)

        row.setTextViewText(R.id.widgetLessonType, lesson.lessonType)
        val typeBackgroundDrawable = ShapeDrawable(OvalShape()).apply {
            paint.color = context.color(lesson.priority.formattedColorRes)
        }
        val size = context.dimenPx(R.dimen.widget_lesson_type_image_size)
        try {
            val lessonTypeBitmap = typeBackgroundDrawable.toBitmap(width = size, height = size)
            row.setImageViewBitmap(R.id.widgetLessonTypeBackground, lessonTypeBitmap)
        } catch (e: IndexOutOfBoundsException) {
            //FIXME: why this happens
        }

        row.setTextViewText(R.id.widgetLessonSubject, lesson.subject)

        val subgroupNumber = context.getString(R.string.display_schedule_item_msg_subgroup, lesson.subgroupNumber.value)
        row.setTextViewText(R.id.widgetLessonSubgroupNumber, subgroupNumber)
        val subgroupNumberTextVisibility = if (lesson.subgroupNumber != SubgroupNumber.ALL) View.VISIBLE else View.GONE
        row.setViewVisibility(R.id.widgetLessonSubgroupNumber, subgroupNumberTextVisibility)

        row.setTextViewText(R.id.widgetLessonStartTime, lesson.startTime.formattedString)

        row.setTextViewText(R.id.widgetLessonEndTime, lesson.endTime.formattedString)

        row.setTextViewText(R.id.widgetLessonAuditories, lesson.auditories.formatAuditories())

        val noteTextVisibility = if (lesson.note.isNotEmpty()) View.VISIBLE else View.GONE
        row.setTextViewText(R.id.widgetLessonNote, lesson.note)
        row.setViewVisibility(R.id.widgetLessonNote, noteTextVisibility)

        return row
    }

    private fun getExamRow(exam: Exam): RemoteViews {
        val layoutRes = when (widgetInfo?.theme) {
            ScheduleWidgetInfo.WidgetTheme.LIGHT -> R.layout.item_widget_schedule_exam_light
            ScheduleWidgetInfo.WidgetTheme.DARK -> R.layout.item_widget_schedule_exam_dark
            else -> {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> R.layout.item_widget_schedule_exam_light
                    Configuration.UI_MODE_NIGHT_YES -> R.layout.item_widget_schedule_exam_dark
                    else -> R.layout.item_widget_schedule_exam_light
                }
            }
        }
        val row = RemoteViews(context.packageName, layoutRes)

        row.setTextViewText(R.id.widgetExamSubject, exam.subject)

        val subgroupNumber = context.getString(R.string.display_schedule_item_msg_subgroup, exam.subgroupNumber.value)
        row.setTextViewText(R.id.widgetExamSubgroup, subgroupNumber)
        val subgroupNumberTextVisibility = if (exam.subgroupNumber != SubgroupNumber.ALL) View.VISIBLE else View.GONE
        row.setViewVisibility(R.id.widgetExamSubgroup, subgroupNumberTextVisibility)

        row.setTextViewText(R.id.widgetExamTime, exam.startTime.formattedString)

        row.setTextViewText(R.id.widgetExamDate, exam.date.formattedString)

        row.setTextViewText(R.id.widgetExamType, exam.lessonType)

        row.setTextViewText(R.id.widgetExamAuditory, exam.auditories.formatAuditories())

        val noteTextVisibility = if (exam.note.isNotEmpty()) View.VISIBLE else View.GONE
        row.setTextViewText(R.id.widgetExamNote, exam.note)
        row.setViewVisibility(R.id.widgetExamNote, noteTextVisibility)

        return row
    }
}