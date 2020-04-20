package com.pechuro.bsuirschedule.feature.lessondetails

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Lesson
import javax.inject.Inject

class LessonDetailsViewModel @Inject constructor() : BaseViewModel() {

    private lateinit var lesson: Lesson

    lateinit var subject: String

    lateinit var time: String

    lateinit var employeePhotoUrl: String

    lateinit var employeeFullName: String

    lateinit var weeks: String

    lateinit var auditory: String

    fun setLesson(lesson: Lesson) {
        this.lesson = lesson
        subject = lesson.subject
        time = with(lesson) { "$startTime - $endTime" }
        employeePhotoUrl = "https://www.grassrootinstitute.org/wp-content/uploads/2013/04/00409335.jpg" // TODO
        employeeFullName = "Asian teacher" // TODO
        weeks = "Weeks: ${lesson.weekNumber.index + 1}" // TODO
        auditory = lesson.auditories.firstOrNull()?.run { "$name-${building.name}" } ?: "" //TODO
    }
}
