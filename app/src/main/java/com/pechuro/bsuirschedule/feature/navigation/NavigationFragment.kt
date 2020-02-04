package com.pechuro.bsuirschedule.feature.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.commit
import com.pechuro.bsuirschedule.ext.thisTag
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerEvent
import com.pechuro.bsuirschedule.feature.start.StartFragment
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
        if (savedInstanceState == null) {
            val fragment = getHomeFragment()
            replaceFragment(fragment)
        }
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
        EventBus.receive<NavigationDrawerEvent>(lifecycleScope) { event ->
            when (event) {
                is NavigationDrawerEvent.OnScheduleClick -> openScheduleFragment(event.schedule)
                is NavigationDrawerEvent.OnTitleClick -> {

                }
            }
        }
    }

    private fun getHomeFragment() = StartFragment.newInstance()

    private fun openScheduleFragment(schedule: Schedule) {

    }

    private fun openStartFragment() {
        replaceFragment(StartFragment.newInstance())
    }

    private fun replaceFragment(
            fragment: BaseFragment,
            isAddToBackStack: Boolean = true
    ) {
        childFragmentManager.commit {
            replace(navigationFragmentHost.id, fragment, fragment.thisTag)
            if (isAddToBackStack) addToBackStack(fragment.thisTag)
        }
    }

    private inline fun doOnNavigationDrawerClosed(crossinline action: () -> Unit) {
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