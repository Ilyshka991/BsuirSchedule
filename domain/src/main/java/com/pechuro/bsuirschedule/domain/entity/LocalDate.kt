package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.Date

fun Calendar.getLocalDate() = LocalDate(
    year = get(Calendar.YEAR),
    month = get(Calendar.MONTH),
    day = get(Calendar.DAY_OF_MONTH)
)

fun LocalDate.toDate() = Calendar.getInstance().apply {
    set(year, month, day, 0, 0, 0)
}.time

fun Date.getLocalDate() = Calendar.getInstance().apply {
    time = this@getLocalDate
}.getLocalDate()

@Parcelize
data class LocalDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Parcelable, Comparable<LocalDate> {

    companion object {

        fun current(): LocalDate {
            val currentCalendar = Calendar.getInstance()
            return currentCalendar.getLocalDate()
        }
    }

    override fun compareTo(other: LocalDate): Int {
        val yearResult = this.year.compareTo(other.year)
        if (yearResult != 0) return yearResult
        val monthResult = this.month.compareTo(other.month)
        if (monthResult != 0) return monthResult
        return this.day.compareTo(other.day)
    }
}