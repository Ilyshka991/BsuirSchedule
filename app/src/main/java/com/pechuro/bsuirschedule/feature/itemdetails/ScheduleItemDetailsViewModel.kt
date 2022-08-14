package com.pechuro.bsuirschedule.feature.itemdetails

import androidx.lifecycle.LiveData
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.common.provider.AppUriProvider
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.UpdateScheduleItem
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.ext.isConsultation
import com.pechuro.bsuirschedule.ext.isExam
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleItemDetailsViewModel @Inject constructor(
    val appUriProvider: AppUriProvider,
    private val getScheduleItem: GetScheduleItem,
    private val getLessonWeeks: GetLessonWeeks,
    private val updateScheduleItem: UpdateScheduleItem
) : BaseViewModel() {

    lateinit var detailsData: LiveData<DetailsData>

    private var isOpenEventSent = false

    fun init(schedule: Schedule, itemId: Long) {
        if (this::detailsData.isInitialized) return
        detailsData = flowLiveData {
            getScheduleItem
                .execute(GetScheduleItem.Params(schedule, itemId))
                .getOrDefault(emptyFlow())
                .distinctUntilChanged()
                .map { scheduleItem ->
                    if (!isOpenEventSent) {
                        isOpenEventSent = true
                        AppAnalytics.report(AppAnalyticsEvent.Details.Opened(scheduleItem))
                    }
                    val details = getDetails(scheduleItem)
                    DetailsData(scheduleItem, details)
                }
        }
    }

    fun updatePriority(newPriority: LessonPriority) {
        launchCoroutine {
            val currentItem = detailsData.value?.scheduleItem as? Lesson ?: return@launchCoroutine
            val updatedItem = when (currentItem) {
                is Lesson.GroupLesson -> currentItem.copy(priority = newPriority)
                is Lesson.EmployeeLesson -> currentItem.copy(priority = newPriority)
            }
            updateScheduleItem.execute(UpdateScheduleItem.Params(updatedItem))
        }
    }

    fun updateNote(note: String) {
        AppAnalytics.report(AppAnalyticsEvent.Details.NoteChanged)
        launchCoroutine {
            val currentItem = detailsData.value?.scheduleItem ?: return@launchCoroutine
            val updatedItem = when (currentItem) {
                is Lesson.GroupLesson -> currentItem.copy(note = note)
                is Lesson.EmployeeLesson -> currentItem.copy(note = note)
                is Exam.GroupExam -> currentItem.copy(note = note)
                is Exam.EmployeeExam -> currentItem.copy(note = note)
                else -> throw IllegalArgumentException("Not supported schedule item: ${currentItem::class.java.simpleName}")
            }
            updateScheduleItem.execute(UpdateScheduleItem.Params(updatedItem))
        }
    }

    private suspend fun getDetails(scheduleItem: ScheduleItem): List<ScheduleItemDetailsInfo> {
        val resultList = mutableListOf<ScheduleItemDetailsInfo>()
        resultList += getStaffInfo(scheduleItem)
        resultList += ScheduleItemDetailsInfo.Time(scheduleItem.startTime, scheduleItem.endTime)
        resultList += getDateInfo(scheduleItem)
        if (!scheduleItem.isConsultation
            && !(scheduleItem.isExam && scheduleItem.subgroupNumber == SubgroupNumber.ALL)
        ) {
            resultList += ScheduleItemDetailsInfo.Subgroup(scheduleItem.subgroupNumber)
        }
        if (scheduleItem is Lesson) {
            resultList += ScheduleItemDetailsInfo.Priority(scheduleItem.priority)
        }
        resultList += getAuditoriesInfo(scheduleItem.auditories)
        resultList += ScheduleItemDetailsInfo.Note(scheduleItem.note)
        return resultList.toList()
    }

    private fun getStaffInfo(scheduleItem: ScheduleItem) = when (scheduleItem) {
        is Lesson.GroupLesson -> ScheduleItemDetailsInfo.EmployeeInfo(scheduleItem.employees)
        is Exam.GroupExam -> ScheduleItemDetailsInfo.EmployeeInfo(scheduleItem.employees)
        is Lesson.EmployeeLesson -> ScheduleItemDetailsInfo.GroupInfo(scheduleItem.studentGroups)
        is Exam.EmployeeExam -> ScheduleItemDetailsInfo.GroupInfo(scheduleItem.studentGroups)
        else -> throw IllegalArgumentException("Not supported type: $scheduleItem")
    }

    private suspend fun getDateInfo(scheduleItem: ScheduleItem) = when (scheduleItem) {
        is Lesson -> {
            val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(scheduleItem))
                .getOrDefault(emptyList())
            ScheduleItemDetailsInfo.LessonDate(
                weekDay = scheduleItem.weekDay,
                weeks = weeks
            )
        }
        is Exam -> ScheduleItemDetailsInfo.ExamDate(scheduleItem.date)
        else -> throw IllegalArgumentException("Not supported type: $scheduleItem")
    }

    private fun getAuditoriesInfo(auditories: List<Auditory>): List<ScheduleItemDetailsInfo> {
        val resultList = mutableListOf<ScheduleItemDetailsInfo>()
        if (auditories.isNotEmpty()) {
            resultList += ScheduleItemDetailsInfo.AuditoryInfoHeader
        }
        resultList += auditories.map {
            ScheduleItemDetailsInfo.AuditoryInfo(it)
        }
        return resultList
    }

    data class DetailsData(
        val scheduleItem: ScheduleItem,
        val details: List<ScheduleItemDetailsInfo>
    )
}
