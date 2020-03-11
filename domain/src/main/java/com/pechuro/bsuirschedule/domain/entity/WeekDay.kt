package com.pechuro.bsuirschedule.domain.entity

enum class WeekDay(val index: Int) {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    companion object {
        const val TOTAL_COUNT = 7

        fun getForIndex(index: Int) = values()[index]
    }
}