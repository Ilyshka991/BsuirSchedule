package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class CheckInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository,
        private val specialityRepository: ISpecialityRepository,
        private val buildingRepository: IBuildingRepository
) : BaseInteractor<Boolean, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams): Boolean {
        return withContext(Dispatchers.IO) {
            val results = listOf(
                    async { employeeRepository.isStored() },
                    async { groupRepository.isStored() },
                    async { specialityRepository.isStored() },
                    async { buildingRepository.isStored() }
            )
            results.awaitAll().foldRight(true) { result, acc ->
                result and acc
            }
        }
    }
}