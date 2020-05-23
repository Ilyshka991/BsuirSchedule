package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ScheduleItemDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks
) : BaseViewModel() {

    private val scheduleItemData = MutableLiveData<ScheduleItem>()

    val detailsData = MediatorLiveData<List<ScheduleItemDetailsInfo>>().apply {
        addSource(scheduleItemData.distinctUntilChanged()) { scheduleItem ->
            launchCoroutine(Dispatchers.IO) {
                val details = getDetails(scheduleItem)
                postValue(details)
            }
        }
    }

    fun init(scheduleItem: ScheduleItem) {
        scheduleItemData.value = scheduleItem
    }

    private suspend fun getDetails(scheduleItem: ScheduleItem): List<ScheduleItemDetailsInfo> {
        val resultList = mutableListOf<ScheduleItemDetailsInfo>()
        resultList += getStaffInfo(scheduleItem)
        resultList += ScheduleItemDetailsInfo.Time(scheduleItem.startTime, scheduleItem.endTime)
        resultList += ScheduleItemDetailsInfo.LessonType(scheduleItem.lessonType)
        resultList += getDateInfo(scheduleItem)
        resultList += ScheduleItemDetailsInfo.Subgroup(scheduleItem.subgroupNumber)
        if (scheduleItem is Lesson) {
            resultList += ScheduleItemDetailsInfo.Priority(scheduleItem.priority)
        }
        resultList += scheduleItem.auditories.map {
            ScheduleItemDetailsInfo.AuditoryInfo(it)
        }
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
}
