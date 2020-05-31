package com.pechuro.bsuirschedule.common

import com.pechuro.bsuirschedule.domain.entity.*

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

    sealed class Navigation : AppAnalyticsEvent() {

        data class ScheduleOpened(val schedule: Schedule) : Navigation()

        data class ScheduleUpdated(val schedule: Schedule) : Navigation()

        data class ScheduleDeleted(val schedule: Schedule) : Navigation()
    }

    sealed class Settings : AppAnalyticsEvent() {

        object Opened : Settings()

        object ThemesOpened : Settings()

        data class ThemeChanged(val theme: AppTheme) : Settings()

        object InformationUpdated : Settings()

        object PrivacyPoliceOpened : Settings()

        object SendFeedbackOpened : Settings()

        object RateAppOpened : Settings()
    }

    sealed class AddSchedule : AppAnalyticsEvent() {

        object Opened : AddSchedule()

        data class ScheduleLoaded(val schedules: List<Schedule>) : AddSchedule()

        object ScheduleLoadFailed : AddSchedule()
    }

    sealed class DisplaySchedule : AppAnalyticsEvent() {

        data class ItemOptionOpened(val scheduleItem: ScheduleItem) : DisplaySchedule()

        data class ItemDeleted(val scheduleItem: ScheduleItem) : DisplaySchedule()

        object ViewSettingsOpened : DisplaySchedule()

        data class ViewTypeChanged(val type: ScheduleDisplayType) : DisplaySchedule()

        data class SubgroupChanged(val subgroupNumber: SubgroupNumber) : DisplaySchedule()

        object CalendarOpened : DisplaySchedule()

        data class CalendarDateSelected(val dayDiff: Long) : DisplaySchedule()

        object CurrentDayBackClicked : DisplaySchedule()
    }

    sealed class Edit : AppAnalyticsEvent() {

        data class Opened(val scheduleItem: ScheduleItem?) : Edit()

        object Saved : Edit()

        object Cancelled : Edit()
    }

    sealed class Details : AppAnalyticsEvent() {

        data class Opened(val scheduleItem: ScheduleItem) : Details()

        data class LocationClicked(val building: Building) : Details()

        object PriorityOpened : Details()

        object NoteChanged : Details()
    }

    sealed class UpdateSchedule : AppAnalyticsEvent() {

        object Updated : UpdateSchedule()

        data class Dismissed(val notRemind: Boolean) : UpdateSchedule()
    }

    sealed class InfoLoad : AppAnalyticsEvent() {

        object Loaded : InfoLoad()

        object Failed : InfoLoad()
    }

    sealed class RateApp : AppAnalyticsEvent() {

        object RateClicked : RateApp()

        object LaterClicked : RateApp()

        object NotRemindClicked : RateApp()
    }

    sealed class Widget : AppAnalyticsEvent() {

        data class ConfigurationOpened(val widgetExist: Boolean) : Widget()

        data class ConfigurationCanceled(val widgetExist: Boolean) : Widget()

        data class ConfigurationApplied(
                val schedule: Schedule,
                val subgroupNumber: SubgroupNumber,
                val theme: ScheduleWidgetInfo.WidgetTheme
        ) : Widget()

        data class Deleted(val schedule: Schedule) : Widget()
    }
}