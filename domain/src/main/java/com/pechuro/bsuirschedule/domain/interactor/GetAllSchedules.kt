package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllSchedules @Inject constructor(
        private val employeeRepository: IScheduleRepository
) : BaseInteractor<Flow<List<Schedule>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = withContext(Dispatchers.IO) {
        employeeRepository.getAllSchedules()
    }
}