package com.pechuro.bsuirschedule.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout


class TabLayout : TabLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setClickEnabled(isEnabled: Boolean) {
        val tabStrip = getChildAt(0) as LinearLayout
        tabStrip.isEnabled = isEnabled
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).isClickable = isEnabled
        }
    }
}