package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var isSwipeEnabled = true

    override fun onTouchEvent(event: MotionEvent) =
            if (isSwipeEnabled) super.onTouchEvent(event) else false

    override fun onInterceptTouchEvent(event: MotionEvent) =
            if (isSwipeEnabled) super.onInterceptTouchEvent(event) else false

}