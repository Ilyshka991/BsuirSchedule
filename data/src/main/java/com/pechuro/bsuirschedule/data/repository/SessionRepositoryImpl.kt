package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.entity.HintDisplayState
import com.pechuro.bsuirschedule.domain.entity.RateAppAskInfo
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.entity.getLocalDate
import com.pechuro.bsuirschedule.domain.entity.toDate
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import com.pechuro.bsuirschedule.local.sharedprefs.LocalHintDisplayState
import com.pechuro.bsuirschedule.local.sharedprefs.LocalRateAppAskInfo
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo.ScheduleType.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo.ScheduleType.GROUP_CLASSES
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo.ScheduleType.GROUP_EXAMS
import com.pechuro.bsuirschedule.local.sharedprefs.SharedPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.*

class SessionRepositoryImpl(
        private val sharedPreferencesManager: SharedPreferencesManager,
        private val scheduleRepository: IScheduleRepository
) : BaseRepository(), ISessionRepository {

    override suspend fun getLastOpenedSchedule(): Flow<Schedule?> =
            sharedPreferencesManager.getLastOpenedSchedule().map {
                when (it?.type) {
                    GROUP_CLASSES -> scheduleRepository.getGroupClassesByName(it.name)
                    GROUP_EXAMS -> scheduleRepository.getGroupExamsByName(it.name)
                    EMPLOYEE_CLASSES -> scheduleRepository.getEmployeeClassesByName(it.name)
                    EMPLOYEE_EXAMS -> scheduleRepository.getEmployeeExamsByName(it.name)
                    else -> null
                }
            }.catch {
                emit(null)
            }

    override suspend fun setLastOpenedSchedule(schedule: Schedule?) {
        val prefsValue = schedule?.run {
            val scheduleType = when (this) {
                is Schedule.GroupClasses -> GROUP_CLASSES
                is Schedule.GroupExams -> GROUP_EXAMS
                is Schedule.EmployeeClasses -> EMPLOYEE_CLASSES
                is Schedule.EmployeeExams -> EMPLOYEE_EXAMS
            }
            LocalScheduleInfo(name, scheduleType)
        }
        sharedPreferencesManager.setLastOpenedSchedule(prefsValue)
    }

    override suspend fun getScheduleDisplayType(): Flow<ScheduleDisplayType> = sharedPreferencesManager
            .getDisplayType(ScheduleDisplayType.DEFAULT.value)
            .map {
                ScheduleDisplayType.getForValue(it)
            }

    override suspend fun setScheduleDisplayType(type: ScheduleDisplayType) {
        sharedPreferencesManager.setDisplayType(type.value)
    }

    override suspend fun getScheduleDisplaySubgroupNumber(): Flow<SubgroupNumber> = sharedPreferencesManager
            .getSubgroupNumber(SubgroupNumber.ALL.value)
            .map {
                SubgroupNumber.getForValue(it)
            }

    override suspend fun setScheduleDisplaySubgroupNumber(number: SubgroupNumber) {
        sharedPreferencesManager.setSubgroupNumber(number.value)
    }

    override suspend fun getAppTheme(): AppTheme {
        val themeName = sharedPreferencesManager.getAppTheme(AppTheme.DEFAULT.name)
        return AppTheme.getForName(themeName)
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        val name = theme.name
        sharedPreferencesManager.setAppTheme(name)
    }

    override suspend fun getNavigationHintDisplayState(): Flow<Boolean> = sharedPreferencesManager
            .getNavigationHintDisplayState(false)

    override suspend fun setNavigationHintDisplayState(shown: Boolean) {
        sharedPreferencesManager.setNavigationHintDisplayState(shown)
    }

    override suspend fun getScheduleHintDisplayState(): Flow<HintDisplayState> = sharedPreferencesManager
            .getScheduleHintDisplayState()
            .map {
                HintDisplayState(
                        lessonHintShown = it?.scheduleHintShown ?: false,
                        examHintShown = it?.examHintShown ?: false
                )
            }

    override suspend fun isRateAppAskInfoSet(): Boolean {
        val localInfo = sharedPreferencesManager.getRateAppAskInfo()
        return localInfo != null
    }

    override suspend fun setScheduleHintDisplayState(state: HintDisplayState) {
        val localState = LocalHintDisplayState(
                scheduleHintShown = state.lessonHintShown,
                examHintShown = state.examHintShown
        )
        sharedPreferencesManager.setScheduleHintDisplayState(localState)
    }

    override suspend fun getRateAppAskInfo(): RateAppAskInfo {
        val localInfo = sharedPreferencesManager.getRateAppAskInfo()
        return localInfo?.run {
            RateAppAskInfo(
                    installDate = Date(installDate).getLocalDate(),
                    shouldAsk = shouldAsk,
                    askLaterDate = askLaterDate?.let { Date(it).getLocalDate() }
            )
        } ?: RateAppAskInfo()
    }

    override suspend fun setRateAppAskInfo(info: RateAppAskInfo) {
        val localInfo = LocalRateAppAskInfo(
                installDate = info.installDate.toDate().time,
                shouldAsk = info.shouldAsk,
                askLaterDate = info.askLaterDate?.toDate()?.time
        )
        sharedPreferencesManager.setRateAppAskInfo(localInfo)
    }
}