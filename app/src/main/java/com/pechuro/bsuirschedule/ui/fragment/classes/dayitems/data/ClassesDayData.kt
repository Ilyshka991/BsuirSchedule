package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.data

import androidx.databinding.ObservableField

class ClassesDayData(subject: String?) {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}