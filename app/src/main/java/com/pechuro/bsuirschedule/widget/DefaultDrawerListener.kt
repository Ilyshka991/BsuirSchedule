package com.pechuro.bsuirschedule.widget

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

interface DefaultDrawerListener : DrawerLayout.DrawerListener {

    override fun onDrawerStateChanged(newState: Int) {}

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

    override fun onDrawerClosed(drawerView: View) {}

    override fun onDrawerOpened(drawerView: View) {}
}