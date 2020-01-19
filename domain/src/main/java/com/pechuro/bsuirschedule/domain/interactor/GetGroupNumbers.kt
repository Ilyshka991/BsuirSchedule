package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetGroupNumbers @Inject constructor(
        private val groupRepository: IGroupRepository
) : BaseInteractor<Flow<List<String>>, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) = channelFlow {
        launch {
            runCatching {
                groupRepository.updateCache()
            }
        }
        launch {
            groupRepository.getAllNumbers().collect {
                send(it)
            }
        }
    }
}