package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.data.ClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesDayInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassesDayViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<ClassesDayData>>()

    fun loadData(info: ClassesDayInformation) {
        compositeDisposable.add(
                repository.getClasses(info.name, info.type, info.day, info.week)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    private fun transformItems(data: List<ScheduleItem>): List<ClassesDayData> {
        val items = mutableListOf<ClassesDayData>()
        data.forEach { items.add(ClassesDayData(it.subject)) }
        return items
    }
}
