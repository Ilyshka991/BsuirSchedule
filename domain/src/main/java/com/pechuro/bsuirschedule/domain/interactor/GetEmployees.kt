package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetEmployees @Inject constructor(
        private val employeeRepository: IEmployeeRepository
) : BaseInteractor<Flow<List<Employee>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = channelFlow {
        launch {
            runCatching {
                employeeRepository.updateCache()
            }
        }
        launch {
            employeeRepository.getAll().collect {
                send(it)
            }
        }
    }.catch {
        Logger.e(it)
    }
}