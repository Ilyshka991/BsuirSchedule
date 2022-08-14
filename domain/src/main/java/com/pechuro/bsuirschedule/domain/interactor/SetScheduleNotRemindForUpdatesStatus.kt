package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class SetScheduleNotRemindForUpdatesStatus @Inject constructor(
    private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, SetScheduleNotRemindForUpdatesStatus.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.setNotRemindForUpdates(
        params.schedule,
        params.notRemind
    )

    data class Params(
        val schedule: Schedule,
        val notRemind: Boolean
    )
}