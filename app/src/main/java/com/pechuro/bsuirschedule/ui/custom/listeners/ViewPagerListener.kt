package com.pechuro.bsuirschedule.ui.custom.listeners

import androidx.viewpager.widget.ViewPager

interface ViewPagerListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}
}