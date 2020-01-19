package com.pechuro.bsuirschedule.common

import android.os.SystemClock
import android.view.View

enum class ClickInterval(val milliseconds: Int) {
    SHORT(200),
    NORMAL(500),
    LONG(1000)
}

class OneFirePerIntervalClickListener(
        private var interval: Int,
        private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < interval) return
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}