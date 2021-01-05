package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class GetLessonWeeks @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<List<WeekNumber>, GetLessonWeeks.Params>() {

    override suspend fun run(params: Params): List<WeekNumber> {
        return scheduleRepository.getLessonWeeks(params.lesson)
    }

    data class Params(
            val lesson: Lesson
    )
}