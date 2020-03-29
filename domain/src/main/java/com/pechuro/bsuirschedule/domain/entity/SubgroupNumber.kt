package com.pechuro.bsuirschedule.domain.entity

enum class SubgroupNumber(val value: Int) {
    ALL(0),
    FIRST(1),
    SECOND(2);

    fun getNextNumber(): SubgroupNumber {
        val nextIndex = (value + 1) % values().size
        return values()[nextIndex]
    }

    companion object {

        fun getForValue(value: Int) = values()[value]
    }
}