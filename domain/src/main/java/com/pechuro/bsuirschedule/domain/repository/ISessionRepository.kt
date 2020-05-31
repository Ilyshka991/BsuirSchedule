package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.*
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

    suspend fun getScheduleHintDisplayState(): Flow<Boolean>

    suspend fun setScheduleHintDisplayState(shown: Boolean)

    suspend fun getRateAppAskInfo(): RateAppAskInfo

    suspend fun setRateAppAskInfo(info: RateAppAskInfo)
}