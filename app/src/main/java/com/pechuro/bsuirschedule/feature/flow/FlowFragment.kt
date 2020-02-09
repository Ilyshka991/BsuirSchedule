package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleComplete
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetEvent
import com.pechuro.bsuirschedule.feature.start.StartFragmentDirections
import kotlinx.android.synthetic.main.fragment_flow.*

class FlowFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
    }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        Navigation.findNavController(requireActivity(), R.id.navigationFragmentHost)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()

        openLoadInfo()
        initViews()
        receiveEvents()
    }

    override fun onBackPressed(): Boolean {
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
            bottomBarParentView.isVisible = isControlsVisible
        }
    }

    private fun initViews() {
        bottomBarMenuButton.setSafeClickListener {
            openNavigationSheet()
        }
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
               // is LoadInfoComplete -> popFragment()
                is AddScheduleComplete -> popFragment()
                is NavigationSheetEvent.OnAddSchedule -> openAddSchedule()
                is NavigationSheetEvent.OnScheduleClick -> {
                }
                is NavigationSheetEvent.OnTitleClick -> {
                }
                is NavigationSheetEvent.OnOpenSettings -> {
                }
                is NavigationSheetEvent.OnScheduleLongClick -> {
                }
                is NavigationSheetEvent.OnTitleLongClick -> {
                }
            }
        }
    }

    private fun openNavigationSheet() {
        navController.navigate(R.id.navigationSheetDestination)
    }

    private fun openLoadInfo() {
        navController.navigate(R.id.loadInfoDestination)
    }

    private fun openAddSchedule() {
        popFragment()
        navController.navigate(StartFragmentDirections.addScheduleAction())
    }

    private fun popFragment() = navController.popBackStack()
}