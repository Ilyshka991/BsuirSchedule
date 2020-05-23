package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import com.pechuro.bsuirschedule.domain.interactor.UpdateScheduleItem
import javax.inject.Inject

class ScheduleItemDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks,
        private val updateScheduleItem: UpdateScheduleItem
) : BaseViewModel() {

    lateinit var scheduleItem: ScheduleItem

    val detailsData: LiveData<List<ScheduleItemDetailsInfo>> = liveData {
        //  val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(scheduleItem)).getOrDefault(emptyList())
        // emit(getDetailsItems(scheduleItem, weeks))
        emit(emptyList())
    }

    private fun getDetailsItems(lesson: Lesson, weeks: List<WeekNumber>): List<ScheduleItemDetailsInfo> {
        val list = mutableListOf(
                ScheduleItemDetailsInfo.Time(lesson.startTime, lesson.endTime),
                getInfoItem(lesson),
                ScheduleItemDetailsInfo.Weeks(weeks)
        )
        if (lesson.auditories.isNotEmpty()) {
            list += ScheduleItemDetailsInfo.AuditoryHeader
        }
        list += lesson.auditories.map { ScheduleItemDetailsInfo.AuditoryInfo(it) }
        list += ScheduleItemDetailsInfo.Note(lesson.note)
        return list
    }

    private fun getInfoItem(lesson: Lesson): ScheduleItemDetailsInfo = when (lesson) {
        is Lesson.GroupLesson -> ScheduleItemDetailsInfo.EmployeeInfo(lesson.employees)
        is Lesson.EmployeeLesson -> TODO()
    }
}
