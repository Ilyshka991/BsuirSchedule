package com.pechuro.bsuirschedule.widget.listeners

import com.google.android.material.tabs.TabLayout

interface TabLayoutListener : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {}

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    override fun onTabSelected(tab: TabLayout.Tab) {}
}