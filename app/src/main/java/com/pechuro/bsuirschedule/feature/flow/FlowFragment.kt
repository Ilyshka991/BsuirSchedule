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
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleCompleteEvent
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoCompleteEvent
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetEvent
import com.pechuro.bsuirschedule.feature.updateschedule.UpdateScheduleSheetArgs
import kotlinx.android.synthetic.main.fragment_flow.*

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()
        if (!viewModel.isInfoLoaded()) openLoadInfo()
        initViews()
        observeData()
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

    private fun observeData() {
        viewModel.availableForUpdateScheduleListData.observeNonNull(viewLifecycleOwner) {
            if (it.isNotEmpty()) openUpdateSchedules(it)
        }
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
                is LoadInfoCompleteEvent -> popFragment()
                is AddScheduleCompleteEvent -> popFragment()
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
        navController.navigate(R.id.loadInfoDestination, null, defaultNavOptions)
    }

    private fun openUpdateSchedules(schedules: List<Schedule>) {
        if (navController.currentDestination?.id == R.id.navigationSheetDestination) return
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

    private fun popFragment() = navController.popBackStack()
}