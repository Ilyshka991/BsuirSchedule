package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.NotificationManager
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.interactor.*
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FlowViewModel @Inject constructor(
        private val checkInfo: CheckInfo,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
        private val notificationManager: NotificationManager,
        private val getLastOpenedSchedule: GetLastOpenedSchedule,
        private val getScheduleDisplayType: GetScheduleDisplayType,
        private val setLastOpenedSchedule: SetLastOpenedSchedule
) : BaseViewModel() {

    private var lastOpenedSchedule: Schedule? = runBlocking {
        getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrNull()?.first()
    }
    var scheduleDisplayType: ScheduleDisplayType = runBlocking {
        getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow()).first()
    }
        private set

    init {
        launchCoroutine {
            getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow()).collect {
                lastOpenedSchedule = it
            }
        }
        launchCoroutine {
            getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow()).collect {
                scheduleDisplayType = it
            }
        }
    }

    fun isInfoLoaded(): Boolean = runBlocking {
        checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }

    fun getLastOpenedSchedule() = lastOpenedSchedule

    fun setLastOpenedSchedule(schedule: Schedule?) {
        lastOpenedSchedule = schedule
        launchCoroutine { setLastOpenedSchedule.execute(SetLastOpenedSchedule.Params(schedule)) }
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