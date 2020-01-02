package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import kotlinx.coroutines.*

class CheckInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository,
        private val specialityRepository: ISpecialityRepository,
        private val buildingRepository: IBuildingRepository
) : BaseInteractor<Boolean, BaseInteractor.NoParams>() {

    override suspend fun runAsync(params: NoParams): Deferred<Boolean> {
        return withContext(Dispatchers.IO) {
            val results = listOf(
                    async { employeeRepository.isStored() },
                    async { groupRepository.isStored() },
                    async { specialityRepository.isStored() },
                    async { buildingRepository.isStored() }
            )
            async {
                results.awaitAll().foldRight(true) { result, acc ->
                    result and acc
                }
            }
        }
    }
}