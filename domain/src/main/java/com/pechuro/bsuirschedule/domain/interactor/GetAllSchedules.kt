package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSchedules @Inject constructor(
    private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Flow<List<Schedule>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = scheduleRepository.getAllSchedules()
}