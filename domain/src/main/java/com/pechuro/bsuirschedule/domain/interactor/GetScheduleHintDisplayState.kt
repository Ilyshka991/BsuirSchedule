package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.HintDisplayState
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleHintDisplayState @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Flow<HintDisplayState>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams): Flow<HintDisplayState> = sessionRepository.getScheduleHintDisplayState()
}