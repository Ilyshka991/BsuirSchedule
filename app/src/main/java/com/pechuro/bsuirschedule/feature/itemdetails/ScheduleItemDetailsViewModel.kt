package com.pechuro.bsuirschedule.feature.itemdetails

import androidx.lifecycle.LiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.*
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
        private val getScheduleItem: GetScheduleItem,
        private val getLessonWeeks: GetLessonWeeks,
        private val updateScheduleItem: UpdateScheduleItem
) : BaseViewModel() {

    lateinit var detailsData: LiveData<DetailsData>

    fun init(schedule: Schedule, itemId: Long) {
        if (this::detailsData.isInitialized) return
        detailsData = flowLiveData {
            getScheduleItem
                    .execute(GetScheduleItem.Params(schedule, itemId))
                    .getOrDefault(emptyFlow())
                    .distinctUntilChanged()
                    .map { scheduleItem ->
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

    private suspend fun getDetails(scheduleItem: ScheduleItem): List<ScheduleItemDetailsInfo> {
        val resultList = mutableListOf<ScheduleItemDetailsInfo>()
        resultList += getStaffInfo(scheduleItem)
        resultList += ScheduleItemDetailsInfo.Time(scheduleItem.startTime, scheduleItem.endTime)
        resultList += getDateInfo(scheduleItem)
        if (!scheduleItem.isConsultation
                && !(scheduleItem.isExam && scheduleItem.subgroupNumber == SubgroupNumber.ALL)) {
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
            val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(scheduleItem)).getOrDefault(emptyList())
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
