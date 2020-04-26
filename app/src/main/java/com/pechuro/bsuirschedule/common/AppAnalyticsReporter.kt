package com.pechuro.bsuirschedule.common

import com.pechuro.bsuirschedule.domain.common.Logger

class AppAnalyticsReporter : AppAnalytics.Reporter {

    override fun report(event: AppAnalyticsEvent) {
        Logger.tag("AnalyticsReporter").v(event.toString())
    }
}