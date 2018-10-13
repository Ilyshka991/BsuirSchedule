package com.pechuro.bsuirschedule.ui.fragment.exam.data

import androidx.databinding.ObservableField

class ExamData(subject: String?) {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}