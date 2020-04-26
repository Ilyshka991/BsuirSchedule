package com.pechuro.bsuirschedule.domain.entity

enum class AppTheme {
    FOLLOW_SYSTEM, LIGHT, DARK, BLACK, INDIGO, TEAL, BLUE_GRAY, BLUE_WHITE, RED, GREEN;

    companion object {
        val DEFAULT = FOLLOW_SYSTEM

        fun getForName(name: String) = valueOf(name)
    }
}