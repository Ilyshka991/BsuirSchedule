package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NavigationActivityViewModel @Inject constructor(
        private val repository: ScheduleRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<Map<Int, List<ScheduleInformation>>>()
    val command = SingleLiveEvent<NavigationEvent>()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        compositeDisposable.add(repository.getSchedules()
                .map {
                    checkUpdate(it)
                    it.toMap()
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    menuItems.value = it
                }, {}))
    }

    fun updateSchedule(schedule: Schedule) =
            compositeDisposable.add(repository.update(schedule)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        command.call(OnScheduleUpdated(schedule.name, schedule.type))
                    }, {
                        command.call(OnScheduleUpdateFail(schedule.name, schedule.type))
                    })
            )

    fun deleteSchedule(name: String, type: Int) {
        compositeDisposable.add(
                repository.delete(name, type)
                        .subscribeOn(Schedulers.io())
                        .subscribe({

                        }, {})
        )
    }

    private fun checkUpdate(schedules: List<Schedule>) {
        schedules
                .filter {
                    it.type == ScheduleTypes.STUDENT_CLASSES ||
                            it.type == ScheduleTypes.STUDENT_EXAMS
                }
                .forEach { info ->
                    compositeDisposable.add(
                            repository.getLastUpdate(info.name)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        val lastUpdate = it.lastUpdateDate
                                        if (lastUpdate != info.lastUpdate) {
                                            command.call(OnRequestUpdate(info))
                                        }
                                    }, {}))
                }
    }

    private fun List<Schedule>.toMap(): Map<Int, List<ScheduleInformation>> {
        val map = mutableMapOf<Int, List<ScheduleInformation>>()

        map[ScheduleTypes.SCHEDULES] =
                this.asSequence()
                        .filter {
                            it.type == ScheduleTypes.STUDENT_CLASSES ||
                                    it.type == ScheduleTypes.EMPLOYEE_CLASSES
                        }
                        .map { ScheduleInformation(it.name, it.type) }
                        .toList()

        map[ScheduleTypes.EXAMS] =
                this.asSequence()
                        .filter {
                            it.type == ScheduleTypes.STUDENT_EXAMS ||
                                    it.type == ScheduleTypes.EMPLOYEE_EXAMS
                        }
                        .map { ScheduleInformation(it.name, it.type) }
                        .toList()
        return map
    }
}