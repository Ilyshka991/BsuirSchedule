package com.pechuro.bsuirschedule.ui.fragment.exam

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.exam.data.BaseExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.EmployeeExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.StudentExamData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExamViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val listItemsLiveData = MutableLiveData<List<BaseExamData>>()

    fun loadData(info: ScheduleInformation) {
        repository.getClasses(info.name, info.type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listItemsLiveData.value = when (info.type) {
                        ScheduleTypes.STUDENT_EXAMS -> transformStudentItems(it)
                        ScheduleTypes.EMPLOYEE_EXAMS -> transformEmployeeItems(it)
                        else -> throw IllegalStateException()
                    }
                }, {})
                .let(compositeDisposable::add)
    }

    private fun transformStudentItems(data: List<ScheduleItem>): List<StudentExamData> {
        val items = mutableListOf<StudentExamData>()
        data.forEach { items.add(StudentExamData(it.lessonType)) }
        return items
    }

    private fun transformEmployeeItems(data: List<ScheduleItem>): List<EmployeeExamData> {
        val items = mutableListOf<EmployeeExamData>()
        data.forEach { items.add(EmployeeExamData(it.lessonType)) }
        return items
    }
}
