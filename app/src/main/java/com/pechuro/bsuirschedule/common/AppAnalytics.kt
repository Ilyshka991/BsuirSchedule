package com.pechuro.bsuirschedule.common

import com.pechuro.bsuirschedule.domain.entity.Schedule

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

    data class ScheduleOpened(val schedule: Schedule) : AppAnalyticsEvent()
}