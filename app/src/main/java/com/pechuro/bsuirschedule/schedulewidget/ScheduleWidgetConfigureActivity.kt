package com.pechuro.bsuirschedule.schedulewidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppThemeManager
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.app
import kotlinx.android.synthetic.main.activity_configure_schedule_widget.*
import java.util.*
import javax.inject.Inject

class ScheduleWidgetConfigureActivity : AppCompatActivity() {

    @Inject
    protected lateinit var widgetRepository: IWidgetRepository
    @Inject
    protected lateinit var appThemeManager: AppThemeManager

    private val widgetId: Int by lazy(LazyThreadSafetyMode.NONE) {
        intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
        appThemeManager.applyToCurrentTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_schedule_widget)
        setResult(RESULT_CANCELED)
        add_button.setOnClickListener {
            onDone()
        }
    }

    private fun onDone() {
        val resultWidgetInfo = getResultWidgetInfo()
        updateCurrentWidget(resultWidgetInfo)
        val resultIntent = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun updateCurrentWidget(info: ScheduleWidgetInfo) {
        widgetRepository.updateScheduleWidget(info)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        ScheduleWidgetProvider.updateWidget(
                context = this,
                appWidgetManager = appWidgetManager,
                widgetInfo = info
        )
    }

    private fun getResultWidgetInfo(): ScheduleWidgetInfo {
        return ScheduleWidgetInfo(
                widgetId = widgetId,
                schedule = Schedule.GroupClasses("", Date(), Group(1, "", Faculty(2, "", ""), Speciality(2, null, EducationForm(3, ""), "", ",", ""), 4), false),
                subgroupNumber = SubgroupNumber.ALL
        )
    }
}