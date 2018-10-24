package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ViewTypes
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.StudentClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.StudentClassesWeekData
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesWeekInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassesItemViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<Pair<ViewTypes, List<BaseData>>>()

    fun loadData(info: ClassesBaseInformation) {
        val viewType: ViewTypes

        val observable = when (info) {
            is ClassesWeekInformation -> {
                viewType = ViewTypes.STUDENT_WEEK
                repository.getClasses(info.name, info.type, info.day, info.subgroupNumber)
            }
            is ClassesDayInformation -> {
                viewType = ViewTypes.STUDENT_DAY
                repository.getClasses(info.name, info.type, info.day, info.week, info.subgroup)
            }
            else -> throw IllegalStateException()
        }

        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val data = when (viewType) {
                        ViewTypes.STUDENT_WEEK -> transformItemsStudentWeek(it)
                        ViewTypes.STUDENT_DAY -> transformItemsStudentDay(it)
                        else -> throw IllegalStateException()
                    }

                    listItemsLiveData.value = Pair(viewType, data)
                }, {}))
    }

    private fun transformItemsStudentDay(data: List<ScheduleItem>): List<StudentClassesDayData> {
        val items = mutableListOf<StudentClassesDayData>()
        data.forEach { items.add(StudentClassesDayData(it.subject)) }
        return items
    }

    private fun transformItemsStudentWeek(data: List<ScheduleItem>): List<StudentClassesWeekData> {
        val items = mutableListOf<StudentClassesWeekData>()
        data.forEach { items.add(StudentClassesWeekData(it.subject)) }
        return items
    }
}
