package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLessonWeeks @Inject constructor(
        private val scheduleRepository: IScheduleRepository
): BaseInteractor<Flow<List<WeekNumber>>, GetLessonWeeks.Params>() {

    override suspend fun run(params: Params): Flow<List<WeekNumber>> {
        return scheduleRepository.getLessonWeeks(params.lesson)
    }

    data class Params(
            val lesson: Lesson
    )
}