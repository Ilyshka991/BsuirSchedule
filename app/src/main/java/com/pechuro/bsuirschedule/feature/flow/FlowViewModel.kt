package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.NotificationManager
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.domain.interactor.GetLastOpenedSchedule
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleDisplayType
import com.pechuro.bsuirschedule.domain.interactor.SetLastOpenedSchedule
import com.pechuro.bsuirschedule.domain.interactor.SetRateAppInitialInfo
import com.pechuro.bsuirschedule.domain.interactor.ShouldAskRateApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FlowViewModel @Inject constructor(
    private val checkInfo: CheckInfo,
    private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
    private val notificationManager: NotificationManager,
    private val getLastOpenedSchedule: GetLastOpenedSchedule,
    private val getScheduleDisplayType: GetScheduleDisplayType,
    private val setLastOpenedSchedule: SetLastOpenedSchedule,
    private val shouldAskRateApp: ShouldAskRateApp,
    private val setRateAppInitialInfo: SetRateAppInitialInfo
) : BaseViewModel() {

    private var lastOpenedSchedule: Schedule? = runBlocking {
        getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrNull()?.first()
    }
    private var scheduleDisplayType: ScheduleDisplayType = runBlocking {
        getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow()).first()
    }

    init {
        launchCoroutine {
            getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
                .collect {
                    lastOpenedSchedule = it
                }
        }
        launchCoroutine {
            getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
                .collect {
                    scheduleDisplayType = it
                }
        }
        launchCoroutine {
            setRateAppInitialInfo.execute(BaseInteractor.NoParams)
        }
    }

    suspend fun isInfoLoaded(): Boolean {
        return checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }

    fun getLastOpenedSchedule(): Schedule? = lastOpenedSchedule

    fun setLastOpenedSchedule(schedule: Schedule?) {
        lastOpenedSchedule = schedule
        launchCoroutine { setLastOpenedSchedule.execute(SetLastOpenedSchedule.Params(schedule)) }
    }

    fun getScheduleDisplayType(): ScheduleDisplayType = scheduleDisplayType

    suspend fun shouldShowRateApp() =
        shouldAskRateApp.execute(BaseInteractor.NoParams).getOrDefault(false)

    suspend fun getAvailableForUpdateSchedules() =
        getAvailableForUpdateSchedules.execute(Params(includeAll = false))
            .getOrDefault(flowOf(emptyList()))
            .debounce(1000)
            .flowOn(Dispatchers.IO)
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