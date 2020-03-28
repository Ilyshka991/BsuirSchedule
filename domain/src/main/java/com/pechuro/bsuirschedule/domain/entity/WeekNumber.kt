package com.pechuro.bsuirschedule.domain.entity

enum class WeekNumber(val index: Int) {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOURTH(3);

    companion object {
        const val TOTAL_COUNT = 4

        fun getForIndex(index: Int) = values()[index]
    }
}