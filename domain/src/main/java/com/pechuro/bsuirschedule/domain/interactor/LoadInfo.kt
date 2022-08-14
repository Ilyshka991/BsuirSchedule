package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LoadInfo @Inject constructor(
    private val employeeRepository: IEmployeeRepository,
    private val groupRepository: IGroupRepository,
    private val specialityRepository: ISpecialityRepository,
    private val buildingRepository: IBuildingRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = withContext(coroutineContext) {
        specialityRepository.updateCache()
        listOf(
            async { employeeRepository.updateCache() },
            async { groupRepository.updateCache() },
            async { buildingRepository.updateCache() }
        )
            .awaitAll()
            .first()
    }
}