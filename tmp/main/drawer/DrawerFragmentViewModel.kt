package com.pechuro.bsuirschedule.feature.main.drawer

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes
import com.pechuro.bsuirschedule.toDelete.ScheduleRepository
import com.pechuro.bsuirschedule.toDelete.entity.Schedule
import com.pechuro.bsuirschedule.feature.main.ScheduleUpdateEvent
import com.pechuro.bsuirschedule.common.BaseViewModel
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import com.pechuro.bsuirschedule.common.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerFragmentViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<Map<Int, List<ScheduleInformation>>>()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        repository.getSchedules()
                .subscribeOn(Schedulers.io())
                .map { it.toMap() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    menuItems.value = it
                }, {})
                .let(compositeDisposable::add)
    }

    fun checkUpdate() {
        repository.getNotUpdatedSchedules()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.forEach { info ->
                        EventBus.publish(ScheduleUpdateEvent.OnRequestUpdate(info))
                    }
                }, {})
                .let(compositeDisposable::add)
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