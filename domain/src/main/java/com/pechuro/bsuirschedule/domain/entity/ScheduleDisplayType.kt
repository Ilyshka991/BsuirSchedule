package com.pechuro.bsuirschedule.domain.entity

enum class ScheduleDisplayType(val value: Int) {
    DAYS(0), WEEKS(1);

    fun getNextType(): ScheduleDisplayType {
        val nextIndex = (value + 1) % values().size
        return values()[nextIndex]
    }

    companion object {
        val DEFAULT = DAYS

        fun getForValue(value: Int) = values()[value]
    }
}