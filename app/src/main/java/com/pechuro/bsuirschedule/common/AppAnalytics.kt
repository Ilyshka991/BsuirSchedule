package com.pechuro.bsuirschedule.common

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleViewType

object AppAnalytics {

    interface Reporter {

        fun report(event: AppAnalyticsEvent)
    }

    @Volatile
    private var reporter: Reporter? = null

    fun set(reporter: Reporter) {
        this.reporter = reporter
    }

    fun report(event: AppAnalyticsEvent) {
        reporter?.report(event)
    }
}

sealed class AppAnalyticsEvent {

    object Navigation {

        data class ScheduleOpened(val schedule: Schedule) : AppAnalyticsEvent()

        data class ScheduleUpdated(val schedule: Schedule) : AppAnalyticsEvent()

        data class ScheduleDeleted(val schedule: Schedule) : AppAnalyticsEvent()
    }

    object Settings {

        object Opened : AppAnalyticsEvent()

        object ThemesOpened : AppAnalyticsEvent()

        data class ThemeChanged(val theme: AppTheme) : AppAnalyticsEvent()

        object InformationUpdated : AppAnalyticsEvent()

        object PrivacyPoliceOpened : AppAnalyticsEvent()

        object SendFeedbackOpened : AppAnalyticsEvent()

        object RateAppOpened : AppAnalyticsEvent()
    }

    object AddSchedule {

        object Opened : AppAnalyticsEvent()

        data class ScheduleLoaded(val schedule: Schedule, val type: ScheduleType) : AppAnalyticsEvent()

        data class ScheduleLoadFailed(val schedule: Schedule) : AppAnalyticsEvent()
    }

    object DisplaySchedule {

        data class ItemOptionOpened(val scheduleItem: ScheduleItem) : AppAnalyticsEvent()

        data class ItemDeleted(val scheduleItem: ScheduleItem) : AppAnalyticsEvent()

        object SettingsOpened : AppAnalyticsEvent()

        data class ViewTypeChanged(val type: DisplayScheduleViewType) : AppAnalyticsEvent()

        data class SubgroupChanged(val subgroupNumber: SubgroupNumber) : AppAnalyticsEvent()

        object CalendarOpened : AppAnalyticsEvent()

        data class CalendarDateSelected(val dayDiff: Int) : AppAnalyticsEvent()

        object BackClicked : AppAnalyticsEvent()
    }

    object Edit {

        data class Opened(val scheduleItem: ScheduleItem?) : AppAnalyticsEvent()

        object Saved : AppAnalyticsEvent()

        object Cancelled : AppAnalyticsEvent()
    }

    object Details {

        data class Opened(val scheduleItem: ScheduleItem) : AppAnalyticsEvent()

        data class LocationClicked(val building: Building) : AppAnalyticsEvent()

        object PriorityOpened : AppAnalyticsEvent()

        object NoteChanged : AppAnalyticsEvent()
    }

    object UpdateSchedule {

        object Updated : AppAnalyticsEvent()

        data class Dismissed(val notRemind: Boolean) : AppAnalyticsEvent()
    }

    object InfoLoad {

        object Loaded : AppAnalyticsEvent()

        object Failed : AppAnalyticsEvent()
    }

    object RateApp {

        object RateClicked : AppAnalyticsEvent()

        object LaterClicked : AppAnalyticsEvent()

        object NotRemindClicked : AppAnalyticsEvent()
    }

    object Widget {

        data class ConfigurationOpened(val widgetExist: Boolean) : AppAnalyticsEvent()

        data class ConfigurationCanceled(val widgetExist: Boolean) : AppAnalyticsEvent()

        data class ConfigurationApplied(
                val schedule: Schedule,
                val subgroupNumber: SubgroupNumber,
                val theme: ScheduleWidgetInfo.WidgetTheme
        ) : AppAnalyticsEvent()

        object Deleted : AppAnalyticsEvent()
    }
}