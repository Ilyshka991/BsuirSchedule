package com.pechuro.bsuirschedule.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.viewpager.widget.ViewPager


class ViewPager : ViewPager {

    var isSwipeEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        @Suppress("NAME_SHADOWING")
        var heightMeasureSpec = heightMeasureSpec
        val mode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (mode == View.MeasureSpec.UNSPECIFIED || mode == View.MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            if (layoutParams.height == WRAP_CONTENT) {
                var height = 0
                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    val h = child.measuredHeight
                    if (h > height) height = h
                }
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent) =
            if (isSwipeEnabled) super.onTouchEvent(event) else false

    override fun onInterceptTouchEvent(event: MotionEvent) =
            if (isSwipeEnabled) super.onInterceptTouchEvent(event) else false

}