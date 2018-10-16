package com.pechuro.bsuirschedule.ui.activity.navigation.adapter

import androidx.databinding.ObservableField

class NavItemData(name: String) {

    val name = ObservableField<String>()

    init {
        this.name.set(name)
    }
}