package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleDisplayType @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Flow<ScheduleDisplayType>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = sessionRepository.getScheduleDisplayType()
}