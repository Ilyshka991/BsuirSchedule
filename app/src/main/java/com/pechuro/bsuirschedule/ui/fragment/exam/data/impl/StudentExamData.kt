package com.pechuro.bsuirschedule.ui.fragment.exam.data.impl

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.ui.fragment.exam.data.BaseExamData

class StudentExamData(subject: String?) : BaseExamData() {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}