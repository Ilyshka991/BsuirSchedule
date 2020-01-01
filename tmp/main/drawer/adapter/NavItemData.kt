package com.pechuro.bsuirschedule.feature.main.drawer.adapter

import androidx.databinding.ObservableField

class NavItemData(name: String) {

    val name = ObservableField<String>()

    init {
        this.name.set(name)
    }
}