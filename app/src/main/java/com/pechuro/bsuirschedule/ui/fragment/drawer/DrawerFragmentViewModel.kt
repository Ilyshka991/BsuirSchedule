package com.pechuro.bsuirschedule.ui.fragment.drawer

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerFragmentViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<Map<Int, List<ScheduleInformation>>>()

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

    private fun checkUpdate(schedules: List<Schedule>) {
        schedules
                .filter {
                    it.type == ScheduleTypes.STUDENT_CLASSES ||
                            it.type == ScheduleTypes.STUDENT_EXAMS
                }
                .forEach { schedule ->
                    compositeDisposable.add(
                            repository.getLastUpdate(schedule.name)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        val lastUpdate = it.lastUpdateDate
                                        if (lastUpdate != schedule.lastUpdate) {
                                            //  status.call(OnRequestUpdate(schedule.getInfo()))
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
                        .map { ScheduleInformation(it.id, it.name, it.type) }
                        .toList()

        map[ScheduleTypes.EXAMS] =
                this.asSequence()
                        .filter {
                            it.type == ScheduleTypes.STUDENT_EXAMS ||
                                    it.type == ScheduleTypes.EMPLOYEE_EXAMS
                        }
                        .map { ScheduleInformation(it.id, it.name, it.type) }
                        .toList()
        return map
    }
}