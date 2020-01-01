package com.pechuro.bsuirschedule.widget.listeners

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

interface DrawerListener : DrawerLayout.DrawerListener {

    override fun onDrawerStateChanged(newState: Int) {}

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

    override fun onDrawerClosed(drawerView: View) {}

    override fun onDrawerOpened(drawerView: View) {}
}