package com.pechuro.bsuirschedule.ui.fragment.list

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.DayScheduleInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItems = ObservableArrayList<ListItemData>()
    val listItemsLiveData = MutableLiveData<List<ListItemData>>()

    fun addItems(data: List<ListItemData>) {
        listItems.clear()
        listItems.addAll(data)
    }

    fun loadData(info: DayScheduleInformation) {
        compositeDisposable.add(
                repository.getClasses(info.group, ScheduleType.STUDENT_CLASSES, info.day, info.week)
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
