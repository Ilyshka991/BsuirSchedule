package com.pechuro.bsuirschedule.ui.custom

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomappbar.BottomAppBar

class BottomAppBar : BottomAppBar {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}