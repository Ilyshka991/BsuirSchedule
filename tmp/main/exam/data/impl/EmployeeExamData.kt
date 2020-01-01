package com.pechuro.bsuirschedule.feature.main.exam.data.impl

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.feature.main.exam.data.BaseExamData

class EmployeeExamData(subject: String?) : BaseExamData() {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}