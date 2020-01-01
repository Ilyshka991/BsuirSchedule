package com.pechuro.bsuirschedule.ext

import java.util.*

fun Calendar.addDays(days: Int): Long {
    add(Calendar.DATE, days)
    return timeInMillis
}