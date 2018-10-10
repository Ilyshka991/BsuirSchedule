package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.utils.toMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NavigationActivityViewModel @Inject constructor(
        private val repository: ScheduleRepository,
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<Map<Int, List<String>>>()

    init {
        loadMenuItems()
        loadInfo()
    }

    private fun loadInfo() {
        compositeDisposable.add(
                groupRepository.getGroups().subscribeOn(Schedulers.io())
                        .subscribe({}, {})
        )
        compositeDisposable.add(
                employeeRepository.getEmployees().subscribeOn(Schedulers.io())
                        .subscribe({}, {})
        )
    }

    private fun loadMenuItems() {
        compositeDisposable.add(repository.getSchedules()
                .map { it.toMap() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    menuItems.value = it
                }, {}))
    }
}