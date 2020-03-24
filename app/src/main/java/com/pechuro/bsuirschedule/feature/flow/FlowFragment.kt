package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleCompleteEvent
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleContainerArgs
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoCompleteEvent
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetEvent
import com.pechuro.bsuirschedule.feature.scheduleitemdetails.ScheduleItemFragmentArgs
import com.pechuro.bsuirschedule.feature.updateschedule.UpdateScheduleSheetArgs
import kotlinx.android.synthetic.main.fragment_flow.*
import kotlinx.coroutines.launch

class FlowFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
    }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        Navigation.findNavController(requireActivity(), R.id.navigationFragmentHost)
    }
    private val defaultNavOptions = navOptions {
        anim {
            enter = R.anim.fragment_open_enter
            exit = R.anim.fragment_open_exit
            popEnter = R.anim.fragment_close_enter
            popExit = R.anim.fragment_close_exit
        }
        launchSingleTop = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartDestination()
        if (savedInstanceState == null && !viewModel.isInfoLoaded()) openLoadInfo()
        initNavigation()
        initViews()
        checkScheduleUpdates()
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

    private fun checkScheduleUpdates() {
        lifecycleScope.launch {
            val availableForUpdateSchedules = viewModel.getAvailableForUpdateSchedules()
            if (availableForUpdateSchedules.isNotEmpty()) {
                openUpdateSchedules(availableForUpdateSchedules)
            }
        }
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
                is LoadInfoCompleteEvent -> popFragment()
                is AddScheduleCompleteEvent -> {
                    event.schedules.firstOrNull()?.let { schedule ->
                        openViewSchedule(schedule)
                    }
                }
                is NavigationSheetEvent.OnAddSchedule -> openAddSchedule()
                is NavigationSheetEvent.OnScheduleClick -> openViewSchedule(event.schedule)
                is NavigationSheetEvent.OnScheduleDeleted -> onScheduleDeleted(event.schedule)
                is NavigationSheetEvent.OnOpenSettings -> {
                }
                is DisplayScheduleEvent.OpenScheduleItem -> openScheduleItemDetails(event.scheduleItem)
            }
        }
    }

    private fun openScheduleItemDetails(scheduleItem: ScheduleItem) {
        val args = ScheduleItemFragmentArgs(scheduleItem).toBundle()
        navController.navigate(R.id.scheduleItemDestination, args, defaultNavOptions)
    }

    private fun openNavigationSheet() {
        navController.navigate(R.id.navigationSheetDestination)
    }

    private fun openLoadInfo() {
        navController.navigate(R.id.loadInfoDestination, null, defaultNavOptions)
    }

    private fun openUpdateSchedules(schedules: List<Schedule>) {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId == R.id.navigationSheetDestination ||
                currentDestinationId == R.id.updateScheduleSheetDestination) return
        navController.navigate(
                R.id.updateScheduleSheetDestination,
                UpdateScheduleSheetArgs(schedules.toTypedArray()).toBundle(),
                defaultNavOptions
        )
    }

    private fun openAddSchedule() {
        popFragment()
        navController.navigate(R.id.addScheduleDestination, null, defaultNavOptions)
    }

    private fun openViewSchedule(schedule: Schedule) {
        if (viewModel.lastOpenedSchedule == schedule) return
        viewModel.lastOpenedSchedule = schedule
        setViewScheduleStartDestination(schedule)
        val arguments = DisplayScheduleContainerArgs(schedule).toBundle()
        navController.navigate(R.id.viewScheduleDestination, arguments, defaultNavOptions)
    }

    private fun onScheduleDeleted(schedule: Schedule) {
        if (viewModel.lastOpenedSchedule == schedule) {
            setDefaultStartDestination()
            viewModel.lastOpenedSchedule = null
        }
    }

    private fun setStartDestination() {
        val lastOpenedSchedule = viewModel.lastOpenedSchedule
        if (lastOpenedSchedule == null) {
            setDefaultStartDestination()
        } else {
            setViewScheduleStartDestination(lastOpenedSchedule)
        }
    }

    private fun setViewScheduleStartDestination(schedule: Schedule) {
        val navGraph = navController.navInflater.inflate(R.navigation.navigation_graph)
        val arguments = DisplayScheduleContainerArgs(schedule).toBundle()
        navGraph.startDestination = R.id.viewScheduleDestination
        try {
            navController.setGraph(navGraph, arguments)
        } catch (e: IllegalStateException) {
            Logger.e(e)
            //TODO: Possible bug: DialogFragment doesn't exist in the FragmentManager
        }
    }

    private fun setDefaultStartDestination() {
        val navGraph = navController.navInflater.inflate(R.navigation.navigation_graph)
        navGraph.startDestination = R.id.startDestination
        try {
            navController.graph = navGraph
        } catch (e: IllegalStateException) {
            Logger.e(e)
            //TODO: Possible bug: DialogFragment doesn't exist in the FragmentManager
        }
    }

    private fun popFragment() = navController.popBackStack()
}