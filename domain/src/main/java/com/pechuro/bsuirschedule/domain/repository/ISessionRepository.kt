package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.entity.HintDisplayState
import com.pechuro.bsuirschedule.domain.entity.RateAppAskInfo
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import kotlinx.coroutines.flow.Flow

interface ISessionRepository {

    suspend fun getLastOpenedSchedule(): Flow<Schedule?>

    suspend fun setLastOpenedSchedule(schedule: Schedule?)

    suspend fun getScheduleDisplayType(): Flow<ScheduleDisplayType>

    suspend fun setScheduleDisplayType(type: ScheduleDisplayType)

    suspend fun getScheduleDisplaySubgroupNumber(): Flow<SubgroupNumber>

    suspend fun setScheduleDisplaySubgroupNumber(number: SubgroupNumber)

    suspend fun getAppTheme(): AppTheme

    suspend fun setAppTheme(theme: AppTheme)

    suspend fun getNavigationHintDisplayState(): Flow<Boolean>

    suspend fun setNavigationHintDisplayState(shown: Boolean)

    suspend fun getScheduleHintDisplayState(): Flow<HintDisplayState>

    suspend fun setScheduleHintDisplayState(state: HintDisplayState)

    suspend fun getRateAppAskInfo(): RateAppAskInfo

    suspend fun setRateAppAskInfo(info: RateAppAskInfo)
}