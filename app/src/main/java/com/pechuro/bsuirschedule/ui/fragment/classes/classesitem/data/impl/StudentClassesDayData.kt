package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData

class StudentClassesDayData(itemId: Int, subject: String?,
                            type: String?, auditory: List<String>?,
                            employee: List<Employee>?, subgroupNumber: Int?,
                            startTime: String?, endTime: String?,
                            note: String?) : BaseClassesData(itemId) {

    val subject = ObservableField<String?>()
    val type = ObservableField<String?>()
    val auditory = ObservableField<List<String>?>()
    val employee = ObservableField<List<Employee>?>()
    val subgroupNumber = ObservableField<Int?>()
    val startTime = ObservableField<String?>()
    val endTime = ObservableField<String?>()
    val note = ObservableField<String?>()

    init {
        this.subject.set(subject)
        this.type.set(type)
        this.auditory.set(auditory)
        this.employee.set(employee)
        this.subgroupNumber.set(subgroupNumber)
        this.startTime.set(startTime)
        this.endTime.set(endTime)
        this.note.set(note)
    }
}