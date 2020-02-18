package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetGroups @Inject constructor(
        private val groupRepository: IGroupRepository
) : BaseInteractor<Flow<List<Group>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = channelFlow {
        launch {
            runCatching {
                groupRepository.updateCache()
            }
        }
        launch {
            groupRepository.getAll().collect {
                send(it)
            }
        }
    }.catch {
        Logger.e(it)
    }
}