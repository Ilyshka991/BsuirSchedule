package com.pechuro.bsuirschedule.util

import android.os.SystemClock

inline fun waitFor(delay: Long) {
    SystemClock.sleep(delay)
}