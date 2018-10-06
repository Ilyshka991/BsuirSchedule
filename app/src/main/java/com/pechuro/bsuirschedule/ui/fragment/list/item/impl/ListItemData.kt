package com.pechuro.bsuirschedule.ui.fragment.list.item.impl

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.ui.fragment.list.item.BaseItemData

class ListItemData(subject: String?) : BaseItemData() {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}