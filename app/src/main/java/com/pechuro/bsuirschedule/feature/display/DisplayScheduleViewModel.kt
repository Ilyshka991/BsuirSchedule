package com.pechuro.bsuirschedule.feature.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pechuro.bsuirschedule.common.LiveEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.entity.WeekNumber.Companion.calculateCurrentWeekNumber
import com.pechuro.bsuirschedule.domain.interactor.*
import com.pechuro.bsuirschedule.ext.addIfEmpty
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItemInfo
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItemInfo.DayClasses
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItemInfo.WeekClasses
import com.pechuro.bsuirschedule.feature.display.data.toGroupKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DisplayScheduleViewModel @Inject constructor(
        private val getScheduleItems: GetScheduleItems,
        private val getScheduleDisplayType: GetScheduleDisplayType,
        private val getScheduleDisplaySubgroupNumber: GetScheduleDisplaySubgroupNumber,
        private val getScheduleHintDisplayState: GetScheduleHintDisplayState,
        private val setScheduleHintDisplayState: SetScheduleHintDisplayState
) : BaseViewModel() {

    lateinit var schedule: Schedule

    val currentWeekNumber: WeekNumber by lazy(LazyThreadSafetyMode.NONE) {
        calculateCurrentWeekNumber()
    }

    val displayTypeData = MediatorLiveData<ScheduleDisplayType>().apply {
        value = runBlocking {
            getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow()).first()
        }
        val displayTypeFlowData = flowLiveData {
            getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
        }
        addSource(displayTypeFlowData) { value = it }
    }

    val hintDisplayState = flowLiveData {
        getScheduleHintDisplayState.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }

    val eventsData = LiveEvent<Event>()

    private val scheduleItemList: LiveData<List<ScheduleItem>> = flowLiveData {
        getScheduleItems.execute(GetScheduleItems.Params(schedule)).getOrDefault(emptyFlow())
    }
    private val displaySubgroupNumber = flowLiveData {
        getScheduleDisplaySubgroupNumber.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }

    fun getItems(info: DisplayScheduleItemInfo) = MediatorLiveData<List<DisplayScheduleItem>>().apply {
        val transformFunction: () -> Unit = {
            launchCoroutine {
                val scheduleItems = scheduleItemList.value ?: return@launchCoroutine
                val resultList = withContext(Dispatchers.IO) {
                    mapToDisplayScheduleItems(
                            scheduleItems = scheduleItems,
                            info = info,
                            subgroupNumber = displaySubgroupNumber.value ?: SubgroupNumber.ALL
                    )
                }
                value = resultList
            }
        }
        addSource(scheduleItemList) { transformFunction() }
        addSource(displaySubgroupNumber) { transformFunction() }
    }

    fun dismissHint() {
        launchCoroutine {
            setScheduleHintDisplayState.execute(SetScheduleHintDisplayState.Params(shown = true))
        }
    }

    fun calculateFirstVisibleItem(items: List<DisplayScheduleItem>): Int {
        val currentDate = LocalDate.current()
        return items.indexOfFirst {
            it is DisplayScheduleItem.Exams && it.scheduleItem.date >= currentDate
        }
    }

    private fun mapToDisplayScheduleItems(
            scheduleItems: List<ScheduleItem>,
            info: DisplayScheduleItemInfo,
            subgroupNumber: SubgroupNumber
    ): List<DisplayScheduleItem> = when (info) {
        is DayClasses -> scheduleItems.mapToDayClasses(info.weekDay, info.weekNumber, subgroupNumber)
        is WeekClasses -> scheduleItems.mapToWeekClasses(info.weekDay, subgroupNumber)
        is DisplayScheduleItemInfo.Exams -> scheduleItems.mapToExams()
    }.addIfEmpty(DisplayScheduleItem.Empty)

    private fun List<ScheduleItem>.mapToDayClasses(
            weekDay: WeekDay,
            weekNumber: WeekNumber,
            subgroupNumber: SubgroupNumber
    ): List<DisplayScheduleItem.DayClasses> = this
            .asSequence()
            .filterIsInstance<Lesson>()
            .filter {
                it.weekDay == weekDay && it.weekNumber == weekNumber
            }
            .filter {
                when {
                    it is Lesson.EmployeeLesson -> true
                    subgroupNumber == SubgroupNumber.ALL -> true
                    it.subgroupNumber == SubgroupNumber.ALL -> true
                    else -> it.subgroupNumber == subgroupNumber
                }
            }
            .sortedBy { it.startTime }
            .map(DisplayScheduleItem::DayClasses)
            .toList()

    private fun List<ScheduleItem>.mapToWeekClasses(
            weekDay: WeekDay,
            subgroupNumber: SubgroupNumber
    ): List<DisplayScheduleItem.WeekClasses> = this
            .asSequence()
            .filterIsInstance<Lesson>()
            .filter { it.weekDay == weekDay }
            .filter {
                when {
                    it is Lesson.EmployeeLesson -> true
                    subgroupNumber == SubgroupNumber.ALL -> true
                    it.subgroupNumber == SubgroupNumber.ALL -> true
                    else -> it.subgroupNumber == subgroupNumber
                }
            }
            .groupBy { it.toGroupKey() }
            .values
            .map { lessons ->
                val lesson = lessons.first()
                DisplayScheduleItem.WeekClasses(
                        scheduleItem = lesson,
                        allScheduleItems = lessons
                )
            }
            .toList()
            .sortedBy { it.scheduleItem.startTime }

    private fun List<ScheduleItem>.mapToExams(): List<DisplayScheduleItem.Exams> = this
            .filterIsInstance<Exam>()
            .sortedWith(compareBy<Exam> { it.date }.thenBy { it.startTime })
            .map(DisplayScheduleItem::Exams)

    sealed class Event {
        data class OnScheduleItemClicked(val data: ScheduleItem) : Event()
        data class OnScheduleItemLongClicked(val data: DisplayScheduleItem) : Event()
    }
}