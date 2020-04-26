package com.pechuro.bsuirschedule.domain.entity

enum class AppTheme {
    LIGHT, DARK;

    companion object {
        val DEFAULT = LIGHT

        fun getForName(name: String) = valueOf(name)
    }
}