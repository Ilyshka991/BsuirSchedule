package com.pechuro.bsuirschedule.ui.fragment.list

import androidx.databinding.ObservableField

class ListItemData(subject: String?) {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}