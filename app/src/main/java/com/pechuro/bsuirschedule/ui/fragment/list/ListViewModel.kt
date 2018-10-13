package com.pechuro.bsuirschedule.ui.fragment.list

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.list.item.impl.ListItemData
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ScheduleInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<ListItemData>>()

    fun loadData(info: ClassesDayInformation) {
        compositeDisposable.add(
                repository.getClasses(info.name, info.type, info.day, info.week)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    fun loadData(info: ScheduleInformation) {
        compositeDisposable.add(
                repository.getClasses(info.name, info.type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    private fun transformItems(data: List<ScheduleItem>): List<ListItemData> {
        val items = mutableListOf<ListItemData>()
        data.forEach { items.add(ListItemData(it.subject)) }
        return items
    }
}
