package com.pechuro.bsuirschedule.feature.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerEvent
import com.pechuro.bsuirschedule.feature.start.StartFragmentEvent
import com.pechuro.bsuirschedule.widget.DefaultDrawerListener
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationFragment : BaseFragment() {

    companion object {

        fun newInstance() = NavigationFragment()
    }

    override val layoutId: Int = R.layout.fragment_navigation

    private val isNavigationDrawerOpen: Boolean
        get() = navigationDrawerParentView.isDrawerOpen(navigationDrawerView)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveEvents()
    }

    override fun onBackPressed(): Boolean {
        if (isNavigationDrawerOpen) {
            closeNavigationDrawer()
            return true
        }
        return false
    }

    private fun receiveEvents() {
        EventBus.receive<StartFragmentEvent>(lifecycleScope) { event ->
            when (event) {
                is StartFragmentEvent.AddSchedule -> openAddSchedule()
            }
        }
        EventBus.receive<NavigationDrawerEvent>(lifecycleScope) { event ->
            when (event) {
                is NavigationDrawerEvent.OnAddSchedule -> openAddSchedule()
                is NavigationDrawerEvent.OnOpenSettings -> {

                }
                is NavigationDrawerEvent.OnScheduleClick -> {

                }
                is NavigationDrawerEvent.OnScheduleLongClick -> {

                }
                is NavigationDrawerEvent.OnTitleClick -> {

                }
                is NavigationDrawerEvent.OnTitleLongClick -> {

                }
            }
        }
    }

    private fun openAddSchedule() {

    }

    private inline fun doOnNavDrawerClosed(crossinline action: () -> Unit) {
        if (isNavigationDrawerOpen) {
            navigationDrawerParentView.addDrawerListener(object : DefaultDrawerListener {
                override fun onDrawerClosed(drawerView: View) {
                    action()
                    navigationDrawerParentView.removeDrawerListener(this)
                }
            })
            closeNavigationDrawer()
        } else {
            action()
        }
    }

    private fun closeNavigationDrawer() {
        navigationDrawerParentView.closeDrawers()
    }
}