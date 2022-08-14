package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textview.MaterialTextView

open class DotAnimatedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr) {

    var maxDotCount = 5
    var animationDelayTimeMs = 400L

    private var initialText: CharSequence = ""

    private val runnable = object : Runnable {
        private var currentDotCount: Int = 0

        override fun run() {
            if (currentDotCount == maxDotCount) {
                currentDotCount = 0
            } else {
                currentDotCount += 1
            }
            text = initialText.appendDots(currentDotCount)
            postDelayed(this, animationDelayTimeMs)
        }
    }

    init {
        initialText = text
    }

    final override fun setVisibility(visibility: Int) {
        if (this.visibility == visibility) return
        when (visibility) {
            View.VISIBLE -> {
                removeCallbacks(runnable)
                runnable.run()
            }
            View.INVISIBLE, View.GONE -> removeCallbacks(runnable)
        }
        super.setVisibility(visibility)
    }

    private fun CharSequence.appendDots(count: Int) = buildString {
        append(this@appendDots)
        repeat(count) {
            append(".")
        }
    }
}