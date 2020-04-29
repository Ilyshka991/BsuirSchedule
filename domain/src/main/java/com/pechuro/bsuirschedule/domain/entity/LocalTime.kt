package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

fun Calendar.getLocalTime() = LocalTime(
        hour = get(Calendar.HOUR_OF_DAY),
        minute = get(Calendar.MINUTE)
)

fun LocalTime.toDate() = Calendar.getInstance().apply {
    set(0, 0, 0, hour, minute, 0)
}.time.apply {
    time -= (time) % 1000
}

fun Date.getLocalTime() = Calendar.getInstance().apply {
    time = this@getLocalTime
}.getLocalTime()

@Parcelize
data class LocalTime(
        val hour: Int,
        val minute: Int
) : Parcelable, Comparable<LocalTime> {

    companion object {

        fun current(): LocalTime {
            val currentCalendar = Calendar.getInstance()
            return currentCalendar.getLocalTime()
        }

        fun of(hour: Int, minute: Int) = LocalTime(
                hour = hour,
                minute = minute
        )
    }

    init {
        require(hour in 0 until 24)
        require(minute in 0 until 60)
    }

    override fun compareTo(other: LocalTime): Int {
        val hourResult = this.hour.compareTo(other.hour)
        return if (hourResult == 0) this.minute.compareTo(other.minute) else hourResult
    }
}