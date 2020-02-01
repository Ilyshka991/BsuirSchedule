package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetEmployeeNames @Inject constructor(
        private val employeeRepository: IEmployeeRepository
) : BaseInteractor<Flow<List<String>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams)  = channelFlow {
        launch {
            runCatching {
                employeeRepository.updateCache()
            }
        }
        launch {
            employeeRepository.getAllNames().collect {
                send(it)
            }
        }
    }
}