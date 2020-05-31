package com.pechuro.bsuirschedule.domain.entity

enum class AppTheme {

    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
    BLACK,
    INDIGO,
    TEAL,
    BLUE_GREY,
    BLUE_LIGHT,
    BROWN,
    ORANGE,
    PURPLE,
    CYAN,
    RED,
    LIME;

    companion object {
        val DEFAULT = FOLLOW_SYSTEM

        fun getAvailable() = listOf(FOLLOW_SYSTEM, LIGHT, DARK, BLACK)

        fun getForName(name: String) = valueOf(name)
    }
}