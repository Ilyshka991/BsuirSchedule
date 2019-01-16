package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.EmployeeClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.EmployeeClassesWeekData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.StudentClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.StudentClassesWeekData
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassesItemViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<BaseClassesData>>()

    fun loadData(info: ClassesBaseInformation) {
        val viewType: ViewTypes

        val observable = when (info) {
            is StudentClassesDayInformation -> {
                viewType = ViewTypes.STUDENT_DAY
                repository.getStudentClasses(info.name, info.type, info.day, info.week, info.subgroup)
            }
            is StudentClassesWeekInformation -> {
                viewType = ViewTypes.STUDENT_WEEK
                repository.getStudentClasses(info.name, info.type, info.day, info.subgroupNumber)
            }
            is EmployeeClassesDayInformation -> {
                viewType = ViewTypes.EMPLOYEE_DAY
                repository.getEmployeeClasses(info.name, info.type, info.day, info.week)
            }
            is EmployeeClassesWeekInformation -> {
                viewType = ViewTypes.EMPLOYEE_WEEK
                repository.getEmployeeClasses(info.name, info.type, info.day)
            }
            else -> throw IllegalStateException()
        }

        observable
                .subscribeOn(Schedulers.io())
                .map {
                    when (viewType) {
                        ViewTypes.STUDENT_DAY -> transformStudentDayItems(it)
                        ViewTypes.STUDENT_WEEK -> transformStudentWeekItems(it)
                        ViewTypes.EMPLOYEE_DAY -> transformEmployeeDayItems(it)
                        ViewTypes.EMPLOYEE_WEEK -> transformEmployeeWeekItems(it)
                    }
                }
                .subscribe({
                    listItemsLiveData.postValue(it)
                }, {})
                .let(compositeDisposable::add)
    }

    private fun transformStudentDayItems(data: List<ScheduleItem>): List<StudentClassesDayData> {
        val items = mutableListOf<StudentClassesDayData>()
        data.forEach {
            with(it) {
                items.add(StudentClassesDayData(id, subject, lessonType, auditories,
                        employees, subgroupNumber, startTime, endTime, note))
            }

        }
        return items
    }

    private fun transformStudentWeekItems(data: List<ScheduleItem>): List<StudentClassesWeekData> {
        val items = mutableListOf<StudentClassesWeekData>()
        data.forEach {
            with(it) {
                items.add(StudentClassesWeekData(id, subject, lessonType, auditories,
                        employees, subgroupNumber, startTime, endTime, note, weekNumber))
            }
        }
        return items
    }

    private fun transformEmployeeDayItems(data: List<ScheduleItem>): List<EmployeeClassesDayData> {
        val items = mutableListOf<EmployeeClassesDayData>()
        data.forEach { items.add(EmployeeClassesDayData(it.id, it.startTime)) }
        return items
    }

    private fun transformEmployeeWeekItems(data: List<ScheduleItem>): List<EmployeeClassesWeekData> {
        val items = mutableListOf<EmployeeClassesWeekData>()
        data.forEach { items.add(EmployeeClassesWeekData(it.id, it.subject)) }
        return items
    }
}

private enum class ViewTypes {
    STUDENT_DAY, STUDENT_WEEK,
    EMPLOYEE_DAY, EMPLOYEE_WEEK
}