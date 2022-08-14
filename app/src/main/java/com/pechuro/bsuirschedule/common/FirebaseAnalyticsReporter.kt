package com.pechuro.bsuirschedule.common

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.AddSchedule
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.Details
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.DisplaySchedule
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.Edit
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.InfoLoad
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.Navigation
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.RateApp
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.Settings
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.UpdateSchedule
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent.Widget
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import java.util.Locale

class FirebaseAnalyticsReporter(
    private val firebaseAnalytics: FirebaseAnalytics
) : AppAnalytics.Reporter {

    companion object {
        private const val EVENT_MAX_SYMBOLS = 40
        private const val PARAM_MAX_SYMBOLS = 100
    }

    override fun report(event: AppAnalyticsEvent) {
        val eventName = buildEvent(event)?.take(EVENT_MAX_SYMBOLS) ?: return
        val params = getParams(event)
        Logger.tag("AnalyticsReporter").i("$eventName${if (!params.isEmpty) ", $params" else ""}")
        firebaseAnalytics.logEvent(eventName, params)
    }

    private fun buildEvent(event: AppAnalyticsEvent): String? {
        val eventName = getEventName(event) ?: return null
        val path = getEventPath(event)?.run { "${this}_" } ?: ""
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
        is Navigation.ScheduleOpened -> "open"
        is Navigation.ScheduleUpdateSuccess -> "update_success"
        is Navigation.ScheduleUpdateFail -> "update_fail"
        is Navigation.ScheduleDeleted -> "delete"

        is Settings.Opened -> "open"
        is Settings.ThemesOpened -> "theme_edit_open"
        is Settings.ThemeChanged -> "theme_change"
        is Settings.InformationUpdateSuccess -> "info_update_success"
        is Settings.InformationUpdateFail -> "info_update_fail"
        is Settings.PrivacyPoliceOpened -> "privacy_police_open"
        is Settings.SendFeedbackOpened -> "send_feedback_open"
        is Settings.RateAppOpened -> "rate_app_open"

        is AddSchedule.Opened -> "open"
        is AddSchedule.ScheduleLoaded -> "load_success"
        is AddSchedule.ScheduleLoadFailed -> "load_fail"

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
        else -> null
    }

    private fun getParams(event: AppAnalyticsEvent): Bundle = when (event) {
        is Navigation.ScheduleOpened -> event.schedule.getInfo()
        is Navigation.ScheduleUpdateSuccess -> event.schedule.getInfo()
        is Navigation.ScheduleUpdateFail -> event.exception.getInfo()
        is Navigation.ScheduleDeleted -> event.schedule.getInfo()

        is Settings.ThemeChanged -> bundleOf("theme" to event.theme.name.lowercase(Locale.ENGLISH))
        is Settings.InformationUpdateFail -> event.exception.getInfo()

        is AddSchedule.ScheduleLoaded -> bundleOf(
            "name" to event.schedule.name.take(PARAM_MAX_SYMBOLS),
            "types" to event.types.joinToString { it.name.lowercase(Locale.ENGLISH) }
                .take(PARAM_MAX_SYMBOLS)
        )
        is AddSchedule.ScheduleLoadFailed -> event.exception.getInfo()

        is DisplaySchedule.ItemOptionOpened -> event.scheduleItem.getInfo()
        is DisplaySchedule.ItemDeleted -> event.scheduleItem.getInfo()
        is DisplaySchedule.ViewTypeChanged -> bundleOf("type" to event.type.name.lowercase(Locale.ENGLISH))
        is DisplaySchedule.SubgroupChanged -> bundleOf(
            "subgroup_number" to event.subgroupNumber.name.lowercase(
                Locale.ENGLISH
            )
        )
        is DisplaySchedule.CalendarDateSelected -> bundleOf("day_difference" to event.dayDiff)

        is Edit.Opened -> event.scheduleItem?.getInfo() ?: bundleOf()

        is Details.Opened -> event.scheduleItem.getInfo()
        is Details.LocationClicked -> bundleOf("building" to event.building.name)

        is UpdateSchedule.Dismissed -> bundleOf("not_remind" to event.notRemind)

        is InfoLoad.Failed -> event.exception.getInfo()

        is Widget.ConfigurationOpened -> bundleOf("new_widget" to !event.widgetExist)
        is Widget.ConfigurationApplied -> bundleOf(
            "name" to event.schedule.name.take(PARAM_MAX_SYMBOLS),
            "type" to when (event.schedule) {
                is Schedule.GroupClasses, is Schedule.EmployeeClasses -> "classes"
                is Schedule.GroupExams, is Schedule.EmployeeExams -> "exams"
            },
            "subgroup_number" to event.subgroupNumber.name.lowercase(Locale.ENGLISH),
            "theme" to event.theme.name.lowercase(Locale.ENGLISH)
        )
        is Widget.ConfigurationCanceled -> bundleOf("new_widget" to !event.widgetExist)
        is Widget.Deleted -> event.schedule.getInfo()
        else -> bundleOf()
    }

    private fun Schedule.getInfo() = bundleOf(
        "name" to name.take(PARAM_MAX_SYMBOLS),
        "type" to when (this) {
            is Schedule.GroupClasses, is Schedule.EmployeeClasses -> "classes"
            is Schedule.GroupExams, is Schedule.EmployeeExams -> "exams"
        }
    )

    private fun ScheduleItem.getInfo() = bundleOf(
        "subject" to subject.take(PARAM_MAX_SYMBOLS),
        "type" to when (this) {
            is Lesson.GroupLesson -> "group_lesson"
            is Lesson.EmployeeLesson -> "employee_lesson"
            is Exam.GroupExam -> "group_exam"
            is Exam.EmployeeExam -> "employee_exam"
            else -> "none"
        }
    )

    private fun Throwable.getInfo() = bundleOf(
        "exception" to (this::class.simpleName?.take(PARAM_MAX_SYMBOLS) ?: "")
    )
}