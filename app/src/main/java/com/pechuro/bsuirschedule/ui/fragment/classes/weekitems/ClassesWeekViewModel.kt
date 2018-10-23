package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.data.ClassesWeekData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassesWeekViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<ClassesWeekData>>()

    fun loadData(info: ClassesWeekInformation) {
        compositeDisposable.add(
                repository.getClasses(info.name, info.type, info.day, info.subgroupNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    private fun transformItems(data: List<ScheduleItem>): List<ClassesWeekData> {
        val items = mutableListOf<ClassesWeekData>()
        data.forEach { items.add(ClassesWeekData(it.subject)) }
        return items
    }
}
