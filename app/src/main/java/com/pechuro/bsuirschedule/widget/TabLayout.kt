package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.android.material.R
import com.google.android.material.tabs.TabLayout


class TabLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.tabStyle
) : TabLayout(context, attrs, defStyleAttr) {

    fun setClickEnabled(isEnabled: Boolean) {
        val tabStrip = getChildAt(0) as LinearLayout
        tabStrip.isEnabled = isEnabled
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).isClickable = isEnabled
        }
    }
}