package com.pechuro.bsuirschedule.ext

import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

fun TextView.addTextListener(listener: (text: String) -> Unit) {
    val onTextChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { text, _, _, _ ->
        listener(text?.toString() ?: "")
    }
    addTextChangedListener(onTextChanged = onTextChanged)
}

fun DrawerLayout.addDrawerListener(
        onDrawerStateChanged: (newState: Int) -> Unit = {},
        onDrawerSlide: (drawerView: View, slideOffset: Float) -> Unit = { _, _ -> },
        onDrawerClosed: (drawerView: View) -> Unit = {},
        onDrawerOpened: (drawerView: View) -> Unit = {}
): DrawerLayout.DrawerListener {
    val listener = object : DrawerLayout.DrawerListener {

        override fun onDrawerStateChanged(newState: Int) {
            onDrawerStateChanged.invoke(newState)
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            onDrawerSlide.invoke(drawerView, slideOffset)
        }

        override fun onDrawerClosed(drawerView: View) {
            onDrawerClosed.invoke(drawerView)
        }

        override fun onDrawerOpened(drawerView: View) {
            onDrawerOpened.invoke(drawerView)
        }
    }
    addDrawerListener(listener)
    return listener
}

fun TabLayout.addOnTabSelectedListener(
        onTabReselected: (tab: TabLayout.Tab) -> Unit = {},
        onTabUnselected: (tab: TabLayout.Tab) -> Unit = { },
        onTabSelected: (tab: TabLayout.Tab) -> Unit = {}
) {
    val listener = object : TabLayout.OnTabSelectedListener {

        override fun onTabReselected(tab: TabLayout.Tab) {
            onTabReselected.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            onTabUnselected.invoke(tab)
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            onTabSelected.invoke(tab)
        }
    }
    addOnTabSelectedListener(listener)
}

fun ViewPager.addOnTabSelectedListener(
        onPageScrollStateChanged: (state: Int) -> Unit = {},
        onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> },
        onPageSelected: (position: Int) -> Unit = {}
) {
    val listener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected.invoke(position)
        }
    }
    addOnPageChangeListener(listener)
}