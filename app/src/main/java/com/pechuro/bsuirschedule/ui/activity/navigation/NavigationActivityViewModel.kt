package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemData
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NavigationActivityViewModel @Inject constructor(
        private val repository: ScheduleRepository) : BaseViewModel() {
    val menuItems = MutableLiveData<List<NavItemData>>()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        compositeDisposable.add(repository.getSchedules()
                .map { list ->
                    val result = mutableListOf<NavItemData>()
                    list.forEach { result.add(NavItemData(it.name)) }
                    return@map result
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    menuItems.value = it
                }, {}))
    }
}