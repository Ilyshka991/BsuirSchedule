package com.pechuro.bsuirschedule.ext

import android.view.View
import com.pechuro.bsuirschedule.common.ClickInterval
import com.pechuro.bsuirschedule.common.OneFirePerIntervalClickListener

inline fun View.setOneFirePerIntervalClickListener(
        interval: ClickInterval = ClickInterval.NORMAL,
        crossinline onClick: (View) -> Unit
) {
    val safeClickListener = OneFirePerIntervalClickListener(interval.milliseconds) {
        onClick(it)
    }
    setOnClickListener(safeClickListener)
}

inline var View.isVisibleOrInvisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }
