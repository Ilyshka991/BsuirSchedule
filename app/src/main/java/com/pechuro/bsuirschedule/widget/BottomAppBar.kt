package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.bottomappbar.BottomAppBar

class BottomAppBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.bottomAppBarStyle
) : BottomAppBar(context, attrs, defStyleAttr) {

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}