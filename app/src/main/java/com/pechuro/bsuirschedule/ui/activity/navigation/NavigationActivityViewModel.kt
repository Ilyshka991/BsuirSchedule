package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.utils.toMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NavigationActivityViewModel @Inject constructor(
        private val repository: ScheduleRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<Map<Int, List<ScheduleInformation>>>()

    init {
        loadMenuItems()
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