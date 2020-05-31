package com.pechuro.bsuirschedule.common

import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.*
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import java.util.*

class AppAnalyticsReporter : AppAnalytics.Reporter {

    override fun report(event: AppAnalyticsEvent) {
        val eventName = buildEvent(event) ?: return
        val params = getParams(event)
        Logger.tag("AnalyticsReporter").v("$eventName${if (params.isNotEmpty()) ", $params" else ""}")
    }

    private fun buildEvent(event: AppAnalyticsEvent): String? {
        val eventName = getEventName(event) ?: return null
        val path = getEventPath(event)?.run { "$this:" } ?: ""
        return "$path$eventName"
    }

    private fun getEventPath(event: AppAnalyticsEvent): String? = when (event) {
        is Navigation -> "navigation"
        is Settings -> "settings"
        is AddSchedule -> "add_schedule"
        is DisplaySchedule -> "display_schedule"
        is Edit -> "edit"
        is Details -> "item_details"
        is UpdateSchedule -> "update_schedule"
        is InfoLoad -> "info_load"
        is RateApp -> "rate_app"
        is Widget -> "widget"
    }

    private fun getEventName(event: AppAnalyticsEvent): String? = when (event) {
        is Navigation.ScheduleOpened -> "schedule_open"
        is Navigation.ScheduleUpdated -> "schedule_update"
        is Navigation.ScheduleDeleted -> "schedule_delete"

        is Settings.Opened -> "open"
        is Settings.ThemesOpened -> "theme_edit_open"
        is Settings.ThemeChanged -> "theme_change"
        is Settings.InformationUpdated -> "info_update"
        is Settings.PrivacyPoliceOpened -> "privacy_police_open"
        is Settings.SendFeedbackOpened -> "send_feedback_open"
        is Settings.RateAppOpened -> "rate_app_open"

        is AddSchedule.Opened -> "open"
        is AddSchedule.ScheduleLoaded -> "schedule_load_success"
        is AddSchedule.ScheduleLoadFailed -> "schedule_load_fail"

        is DisplaySchedule.ItemOptionOpened -> "item_options_open"
        is DisplaySchedule.ItemDeleted -> "item_delete"
        is DisplaySchedule.ViewSettingsOpened -> "view_options_open"
        is DisplaySchedule.ViewTypeChanged -> "view_type_change"
        is DisplaySchedule.SubgroupChanged -> "subgroup_change"
        is DisplaySchedule.CalendarOpened -> "calendar_open"
        is DisplaySchedule.CalendarDateSelected -> "calendar_date_select"
        is DisplaySchedule.CurrentDayBackClicked -> "back_to_current_day"

        is Edit.Opened -> "open"
        is Edit.Saved -> "save"
        is Edit.Cancelled -> "cancel"

        is Details.Opened -> "open"
        is Details.LocationClicked -> "location_click"
        is Details.PriorityOpened -> "priority_change"
        is Details.NoteChanged -> "note_change"

        is UpdateSchedule.Updated -> "success"
        is UpdateSchedule.Dismissed -> "cancel"

        is InfoLoad.Loaded -> "success"
        is InfoLoad.Failed -> "fail"

        is RateApp.RateClicked -> "rate"
        is RateApp.LaterClicked -> "later"
        is RateApp.NotRemindClicked -> "not_remind"

        is Widget.ConfigurationOpened -> "config_open"
        is Widget.ConfigurationApplied -> "config_apply"
        is Widget.ConfigurationCanceled -> "config_cancel"
        is Widget.Deleted -> "delete"
    }

    private fun getParams(event: AppAnalyticsEvent): Map<String, Any> = when (event) {
        is Navigation.ScheduleOpened -> mapOf("schedule" to event.schedule.getInfo())
        is Navigation.ScheduleUpdated -> mapOf("schedule" to event.schedule.getInfo())
        is Navigation.ScheduleDeleted -> mapOf("schedule" to event.schedule.getInfo())

        is Settings.ThemeChanged -> mapOf("theme" to event.theme.name.toLowerCase(Locale.ENGLISH))

        is AddSchedule.ScheduleLoaded -> mapOf("schedule" to event.schedules.map { it.getInfo() })

        is DisplaySchedule.ItemOptionOpened -> mapOf("item" to event.scheduleItem.getInfo())
        is DisplaySchedule.ItemDeleted -> mapOf("item" to event.scheduleItem.getInfo())
        is DisplaySchedule.ViewTypeChanged -> mapOf("type" to event.type.name.toLowerCase(Locale.ENGLISH))
        is DisplaySchedule.SubgroupChanged -> mapOf("subgroup_number" to event.subgroupNumber.name.toLowerCase(Locale.ENGLISH))
        is DisplaySchedule.CalendarDateSelected -> mapOf("day_difference" to event.dayDiff)

        is Edit.Opened -> mapOf("item" to (event.scheduleItem?.getInfo() ?: "none"))

        is Details.Opened -> mapOf("item" to event.scheduleItem.getInfo())
        is Details.LocationClicked -> mapOf("building" to event.building.name)

        is UpdateSchedule.Dismissed -> mapOf("not_remind" to event.notRemind)

        is Widget.ConfigurationOpened -> mapOf("new_widget" to !event.widgetExist)
        is Widget.ConfigurationApplied -> mapOf(
                "schedule" to event.schedule.getInfo(),
                "subgroup_number" to event.subgroupNumber.name.toLowerCase(Locale.ENGLISH),
                "theme" to event.theme.name.toLowerCase(Locale.ENGLISH)
        )
        is Widget.ConfigurationCanceled -> mapOf("new_widget" to !event.widgetExist)
        is Widget.Deleted -> mapOf("schedule" to event.schedule.getInfo())
        else -> emptyMap()
    }

    private fun Schedule.getInfo() = mapOf(
            "name" to name,
            "type" to when (this) {
                is Schedule.GroupClasses, is Schedule.EmployeeClasses -> "classes"
                is Schedule.GroupExams, is Schedule.EmployeeExams -> "exams"
            }
    )

    private fun ScheduleItem.getInfo() = mapOf(
            "subject" to subject,
            "type" to when (this) {
                is Lesson.GroupLesson -> "group_lesson"
                is Lesson.EmployeeLesson -> "employee_lesson"
                is Exam.GroupExam -> "group_exam"
                is Exam.EmployeeExam -> "employee_exam"
                else -> ""
            }
    )
}