package com.pechuro.bsuirschedule.feature.navigation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleComplete
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoComplete
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerEvent
import com.pechuro.bsuirschedule.feature.start.StartFragmentDirections
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_navigation

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationViewModel::class)
    }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        Navigation.findNavController(requireActivity(), R.id.navigationFragmentHost)
    }

    private val isNavigationDrawerOpen: Boolean
        get() = navigationDrawerParentView.isDrawerOpen(navigationDrawerView)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()
        receiveEvents()
        if (!viewModel.isInfoLoaded()) openLoadInfo()
    }

    override fun onBackPressed(): Boolean {
        if (isNavigationDrawerOpen) {
            closeNavigationDrawer()
            return true
        }
        when (navController.currentDestination?.id) {
            R.id.loadInfoDestination -> return false
        }
        if (navController.navigateUp()) return true
        return false
    }

    private fun initNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isControlsVisible = when (destination.id) {
                R.id.addScheduleDestination -> false
                R.id.loadInfoDestination -> false
                else -> true
            }
            bottomBarBackFab.isVisible = isControlsVisible
            bottomBarParentView.isVisible = isControlsVisible
            val drawerLockMode = if (isControlsVisible) {
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
            navigationDrawerParentView.setDrawerLockMode(drawerLockMode)
        }
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
                is OnViewCreated -> closeNavigationDrawer()
                is LoadInfoComplete -> popFragment()
                is AddScheduleComplete -> popFragment()
                is NavigationDrawerEvent.OnAddSchedule -> openAddSchedule()
                is NavigationDrawerEvent.OnScheduleClick -> {
                }
                is NavigationDrawerEvent.OnTitleClick -> {
                }
                is NavigationDrawerEvent.OnOpenSettings -> {
                }
                is NavigationDrawerEvent.OnScheduleLongClick -> {
                }
                is NavigationDrawerEvent.OnTitleLongClick -> {
                }
            }
        }
    }

    private fun openLoadInfo() {
        navController.navigate(R.id.loadInfoDestination)
    }

    private fun openAddSchedule() {
        navController.navigate(StartFragmentDirections.addScheduleAction())
    }

    private fun popFragment() = navController.popBackStack()

    private fun closeNavigationDrawer() {
        navigationDrawerParentView.post { navigationDrawerParentView.closeDrawers() }
    }
}