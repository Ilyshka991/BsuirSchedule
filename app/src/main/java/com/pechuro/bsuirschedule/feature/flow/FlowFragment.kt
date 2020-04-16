package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleCompleteEvent
import com.pechuro.bsuirschedule.feature.datepicker.DisplayScheduleDatePickerSheetArgs
import com.pechuro.bsuirschedule.feature.datepicker.ScheduleDatePickedEvent
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleContainerArgs
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.examdetails.ExamDetailsFragmentArgs
import com.pechuro.bsuirschedule.feature.itemoptions.EditScheduleItemEvent
import com.pechuro.bsuirschedule.feature.itemoptions.ScheduleItemOptionsSheetArgs
import com.pechuro.bsuirschedule.feature.lessondetails.LessonDetailsFragmentArgs
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoCompleteEvent
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragmentArgs
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetEvent
import com.pechuro.bsuirschedule.feature.scheduleoptions.DisplayScheduleOptionsSheetArgs
import com.pechuro.bsuirschedule.feature.update.UpdateScheduleSheetArgs
import kotlinx.android.synthetic.main.fragment_flow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
        initNavigation()
        setStartDestination()
        lifecycleScope.launch(Dispatchers.IO) {
            if (savedInstanceState == null && !viewModel.isInfoLoaded()) openLoadInfo()
            checkScheduleUpdates()
        }
        initViews()
        receiveEvents()
    }

    override fun onResume() {
        super.onResume()
        requireView().post { updateLayoutState() }
    }

    override fun onBackPressed(): Boolean {
        when (navController.currentDestination?.id) {
            R.id.loadInfoDestination -> return false
        }
        if (navController.navigateUp()) return true
        return false
    }

    private fun initNavigation() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            requireView().post { updateLayoutState() }
        }
    }

    private fun initViews() {
        bottomBarMenuButton.setSafeClickListener {
            openNavigationSheet()
        }
        bottomBarDisplayOptionsButton.setSafeClickListener {
            val schedule = viewModel.getLastOpenedSchedule() ?: return@setSafeClickListener
            openDisplayScheduleOptions(schedule)
        }
        bottomBarGoToDateButton.setSafeClickListener {
            EventBus.send(FlowFragmentEvent.DisplayScheduleGoToDate)
        }
        bottomBarAddScheduleItemButton.setSafeClickListener {
            val schedule = viewModel.getLastOpenedSchedule() ?: return@setSafeClickListener
            openAddScheduleItem(schedule)
        }
    }

    private suspend fun checkScheduleUpdates() {
        val availableForUpdateSchedules = viewModel.getAvailableForUpdateSchedules()
        if (availableForUpdateSchedules.isNotEmpty()) {
            openUpdateSchedules(availableForUpdateSchedules)
        }
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(eventCoroutineScope) { event ->
            when (event) {
                is LoadInfoCompleteEvent -> popFragment()
                is AddScheduleCompleteEvent -> {
                    val schedules = event.schedules
                    if (schedules.isEmpty()) {
                        popFragment()
                    } else {
                        val schedule = schedules.first()
                        openViewSchedule(schedule, skipIfOpened = false)
                    }
                }
                is NavigationSheetEvent.OnAddSchedule -> openAddSchedule()
                is NavigationSheetEvent.OnScheduleClick -> openViewSchedule(event.schedule)
                is NavigationSheetEvent.OnScheduleDeleted -> onScheduleDeleted(event.schedule)
                is NavigationSheetEvent.OnOpenSettings -> {
                }
                is DisplayScheduleEvent.OpenScheduleItemDetails -> openScheduleItemDetails(event.data)
                is DisplayScheduleEvent.OpenScheduleItemOptions -> openScheduleItemOptions(event.data)
                is DisplayScheduleEvent.OnPositionChanged -> onDisplaySchedulePositionChanged(event.position)
                is DisplayScheduleEvent.OpenDatePicker -> openDatePicker(event.startDate, event.endDate, event.currentDate)
                is ScheduleDatePickedEvent -> popFragment()
                is EditScheduleItemEvent -> {
                    popFragment()
                    val schedule = viewModel.getLastOpenedSchedule() ?: return@receive
                    openEditScheduleItem(schedule, event.scheduleItems)
                }
            }
        }
    }

    private fun openScheduleItemOptions(data: DisplayScheduleItem) {
        val arguments = ScheduleItemOptionsSheetArgs(data).toBundle()
        navController.navigate(R.id.scheduleItemOptionsDestination, arguments, defaultNavOptions)
    }

    private fun openAddScheduleItem(schedule: Schedule) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = emptyArray()
        ).toBundle()
        navController.navigate(R.id.modifyScheduleItemDestination, arguments, defaultNavOptions)
    }

    private fun openEditScheduleItem(schedule: Schedule, scheduleItems: List<ScheduleItem>) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = scheduleItems.toTypedArray()
        ).toBundle()
        navController.navigate(R.id.modifyScheduleItemDestination, arguments, defaultNavOptions)
    }

    private fun openScheduleItemDetails(data: DisplayScheduleItem) {
        when (val scheduleItem = data.scheduleItem) {
            is Lesson -> openScheduleLessonDetails(scheduleItem)
            is Exam -> openScheduleExamDetails(scheduleItem)
        }
    }

    private fun openScheduleExamDetails(exam: Exam) {
        val arguments = ExamDetailsFragmentArgs(exam).toBundle()
        navController.navigate(R.id.examDetailsDestination, arguments, defaultNavOptions)
    }

    private fun openScheduleLessonDetails(lesson: Lesson) {
        val arguments = LessonDetailsFragmentArgs(lesson).toBundle()
        navController.navigate(R.id.lessonDetailsDestination, arguments, defaultNavOptions)
    }

    private fun openDatePicker(
            startDate: Date,
            endDate: Date,
            currentDate: Date
    ) {
        val arguments = DisplayScheduleDatePickerSheetArgs(startDate, endDate, currentDate).toBundle()
        navController.navigate(R.id.displayScheduleDatePickerDestination, arguments, defaultNavOptions)
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

    private fun openDisplayScheduleOptions(schedule: Schedule) {
        val arguments = DisplayScheduleOptionsSheetArgs(schedule).toBundle()
        navController.navigate(R.id.displayScheduleOptionsDestination, arguments, defaultNavOptions)
    }

    private fun openViewSchedule(schedule: Schedule, skipIfOpened: Boolean = true) {
        if (skipIfOpened && viewModel.getLastOpenedSchedule() == schedule) return
        viewModel.setLastOpenedSchedule(schedule)
        setViewScheduleStartDestination(schedule)
        val arguments = DisplayScheduleContainerArgs(schedule).toBundle()
        navController.navigate(R.id.viewScheduleDestination, arguments, defaultNavOptions)
    }

    private fun onScheduleDeleted(schedule: Schedule) {
        if (viewModel.getLastOpenedSchedule() == schedule) {
            viewModel.setLastOpenedSchedule(null)
            setDefaultStartDestination()
        }
    }

    private fun setStartDestination() {
        val lastOpenedSchedule = viewModel.getLastOpenedSchedule()
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
        requireView().post { updateLayoutState() }

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
        requireView().post { updateLayoutState() }
    }

    private fun popFragment() = navController.popBackStack()

    private fun updateLayoutState() {
        val isControlsVisible = when (navController.currentDestination?.id) {
            R.id.addScheduleDestination,
            R.id.modifyScheduleItemDestination,
            R.id.loadInfoDestination -> false
            else -> true
        }
        updateFabState()
        if (!isControlsVisible) bottomBarFab.hide()
        updateBottomBarState()
        bottomBarParentView.isVisible = isControlsVisible
    }

    private fun updateFabState() {
        val fabState = when (viewModel.getLastOpenedSchedule()) {
            is Schedule.EmployeeClasses, is Schedule.GroupClasses -> FabActionState.DISPLAY_SCHEDULE_BACK
            null -> FabActionState.ADD_SCHEDULE
            else -> null
        }
        if (fabState == FabActionState.ADD_SCHEDULE) {
            bottomBarFab.show()
        } else {
            bottomBarFab.hide()
        }
        if (fabState != null) {
            bottomBarFab.setImageDrawable(ContextCompat.getDrawable(requireContext(), fabState.iconRes))
            bottomBarFab.setSafeClickListener {
                when (fabState) {
                    FabActionState.DISPLAY_SCHEDULE_BACK -> EventBus.send(FlowFragmentEvent.DisplayScheduleSetToday)
                    FabActionState.ADD_SCHEDULE -> openAddSchedule()
                }
            }
        } else {
            bottomBarFab.setOnClickListener(null)
        }
    }

    private fun updateBottomBarState() {
        when (viewModel.getLastOpenedSchedule()) {
            is Schedule.EmployeeClasses, is Schedule.GroupClasses -> {
                bottomBarDisplayOptionsButton.isVisible = true
                bottomBarGoToDateButton.isVisible = viewModel.getScheduleDisplayType() == ScheduleDisplayType.DAYS
                bottomBarAddScheduleItemButton.isVisible = viewModel.getScheduleDisplayType() == ScheduleDisplayType.DAYS
            }
            is Schedule.EmployeeExams, is Schedule.GroupExams -> {
                bottomBarDisplayOptionsButton.isVisible = false
                bottomBarGoToDateButton.isVisible = false
                bottomBarAddScheduleItemButton.isVisible = true
            }
            else -> {
                bottomBarDisplayOptionsButton.isVisible = false
                bottomBarGoToDateButton.isVisible = false
                bottomBarAddScheduleItemButton.isVisible = false
            }
        }
    }

    private fun onDisplaySchedulePositionChanged(position: Int) {
        requireView().post {
            if (position != 0) {
                bottomBarFab.show()
            } else {
                bottomBarFab.hide()
            }
        }
    }
}