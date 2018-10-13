package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.data

import androidx.databinding.ObservableField

class ClassesWeekData(subject: String?) {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}