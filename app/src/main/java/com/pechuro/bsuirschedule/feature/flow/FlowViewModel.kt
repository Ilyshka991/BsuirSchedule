package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.NotificationManager
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.domain.interactor.GetLastOpenedSchedule
import com.pechuro.bsuirschedule.domain.interactor.SetLastOpenedSchedule
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FlowViewModel @Inject constructor(
        private val checkInfo: CheckInfo,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
        private val notificationManager: NotificationManager,
        private val getLastOpenedSchedule: GetLastOpenedSchedule,
        private val setLastOpenedSchedule: SetLastOpenedSchedule
) : BaseViewModel() {

    var lastOpenedSchedule: Schedule?
        get() = runBlocking { getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrNull() }
        set(value) {
            launchCoroutine { setLastOpenedSchedule.execute(SetLastOpenedSchedule.Params(value)) }
        }

    fun isInfoLoaded(): Boolean = runBlocking {
        checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }

    suspend fun getAvailableForUpdateSchedules() =
            getAvailableForUpdateSchedules.execute(Params(includeAll = false))
                    .getOrDefault(flowOf(emptyList()))
                    .debounce(1000)
                    .first()
                    .also {
                        clearUpdateNotifications(it)
                    }

    private fun clearUpdateNotifications(schedules: List<Schedule>) {
        schedules
                .distinctBy { it.name }
                .forEach {
                    notificationManager.dismissUpdateAvailable(it)
                }
    }
}