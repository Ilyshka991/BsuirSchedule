package com.pechuro.bsuirschedule.ext

import android.os.Build

inline fun doIfVersionGreaterThan(minVersion: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > minVersion) block()
}