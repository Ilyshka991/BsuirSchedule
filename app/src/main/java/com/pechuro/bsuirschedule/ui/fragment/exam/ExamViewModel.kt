package com.pechuro.bsuirschedule.ui.fragment.exam

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.activity.navigation.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.exam.data.ExamData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExamViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<ExamData>>()

    fun loadData(info: ScheduleInformation) {
        compositeDisposable.add(
                repository.getClasses(info.name, info.type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listItemsLiveData.value = transformItems(it)
                        }, {}))
    }

    private fun transformItems(data: List<ScheduleItem>): List<ExamData> {
        val items = mutableListOf<ExamData>()
        data.forEach { items.add(ExamData(it.lessonType)) }
        return items
    }
}
