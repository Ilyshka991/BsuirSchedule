package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastOpenedSchedule @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Flow<Schedule?>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams): Flow<Schedule?> = sessionRepository.getLastOpenedSchedule()
}