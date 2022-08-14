package com.pechuro.bsuirschedule.ext

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import com.bsuir.pechuro.bsuirschedule.R

private const val FAST_ANIMATION_DURATION_MS: Long = 150
private const val DEFAULT_ANIMATION_DURATION_MS: Long = 250

private val accelerateInterpolator = AccelerateInterpolator()
private val linearInterpolator = LinearInterpolator()

fun View.animateAlpha(fromAlpha: Float, toAlpha: Float): ValueAnimator = ObjectAnimator
    .ofFloat(this, "alpha", fromAlpha, toAlpha)
    .apply {
        setTag(R.id.tagCurrentAnimator, this)
        duration = DEFAULT_ANIMATION_DURATION_MS
        interpolator = accelerateInterpolator
    }