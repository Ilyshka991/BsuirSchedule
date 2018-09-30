package com.pechuro.bsuirschedule.ui.fragment.list

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.complex.Classes
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItems = ObservableArrayList<ListItemData>()
    val listItemsLiveData = MutableLiveData<List<ListItemData>>()

    init {
        loadData()
    }

    fun addItems(data: List<ListItemData>) {
        listItems.clear()
        listItems.addAll(data)
    }

    private fun loadData() {
        compositeDisposable.add(
                repository.getClasses("750502", ScheduleType.STUDENT_CLASSES)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    private fun transformItems(data: Classes): List<ListItemData> {
        val items = mutableListOf<ListItemData>()
        data.schedule.forEach { items.add(ListItemData(it.subject)) }
        return items
    }
}
