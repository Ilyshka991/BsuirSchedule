package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import javax.inject.Inject

class LessonDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks
) : BaseViewModel() {

    lateinit var lesson: Lesson

    val detailsItems: LiveData<List<DetailsItem>> = liveData {
        val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(lesson)).getOrDefault(emptyList())
        emit(getDetailsItems(lesson, weeks))
    }

    private fun getDetailsItems(lesson: Lesson, weeks: List<WeekNumber>): List<DetailsItem> {
        val list = mutableListOf(
                DetailsItem.Time(lesson.startTime, lesson.endTime),
                getInfoItem(lesson),
                DetailsItem.Weeks(weeks)
        )
        if (lesson.auditories.isNotEmpty()) {
            list += DetailsItem.LocationHeader
        }
        list += lesson.auditories.map { DetailsItem.LocationItem(it) }
        list += DetailsItem.LessonFooter(lesson.note) { onNoteChanged(it) }
        return list
    }

    private fun onNoteChanged(note: String) {
        if (note == lesson.note) {
            return
        }
        TODO("Not yet implemented")
    }

    private fun getInfoItem(lesson: Lesson): DetailsItem = when (lesson) {
        is Lesson.GroupLesson -> DetailsItem.EmployeeInfo(lesson.employees)
        is Lesson.EmployeeLesson -> TODO()
    }
}
