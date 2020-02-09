package com.pechuro.bsuirschedule.ext

import android.animation.Animator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.ClickInterval
import com.pechuro.bsuirschedule.common.OneFirePerIntervalClickListener

inline fun View.setSafeClickListener(
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

fun View.setVisibleWithAlpha(isVisible: Boolean): Animator {
    val previousAnimator = getTag(R.id.tagCurrentAnimator) as? Animator
    previousAnimator?.cancel()
    val animator = if (isVisible) {
        animateAlpha(alpha, 1F).apply {
            doOnStart { visibility = View.VISIBLE }
        }
    } else {
        animateAlpha(alpha, 0F).apply {
            doOnEnd { visibility = View.GONE }
        }
    }
    animator.start()
    return animator
}