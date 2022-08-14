package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetAuditories @Inject constructor(
    private val buildingRepository: IBuildingRepository
) : BaseInteractor<Flow<List<Auditory>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = buildingRepository.getAllAuditories()
        .catch {
            Logger.e(it)
        }
}