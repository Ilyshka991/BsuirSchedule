package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetScheduleDisplaySubgroupNumber @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetScheduleDisplaySubgroupNumber.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setScheduleDisplaySubgroupNumber(params.subgroupNumber)
    }

    data class Params(
            val subgroupNumber: SubgroupNumber
    )
}