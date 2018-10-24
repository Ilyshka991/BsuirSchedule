package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data

import androidx.databinding.ObservableField

class StudentClassesDayData(subject: String?) : BaseData() {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}