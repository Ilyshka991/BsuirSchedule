package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.currentFragment
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleCompleteEvent
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleFragmentContainer
import com.pechuro.bsuirschedule.feature.datepicker.DisplayScheduleDatePickerSheet
import com.pechuro.bsuirschedule.feature.datepicker.DisplayScheduleDatePickerSheetArgs
import com.pechuro.bsuirschedule.feature.datepicker.ScheduleDatePickedEvent
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleContainer
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleEvent
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.itemoptions.EditScheduleItemEvent
import com.pechuro.bsuirschedule.feature.itemoptions.ScheduleItemOptionsSheet
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoCompleteEvent
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoFragment
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragment
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragmentArgs
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheet
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetEvent
import com.pechuro.bsuirschedule.feature.scheduleoptions.DisplayScheduleOptionsSheet
import com.pechuro.bsuirschedule.feature.stafflist.StaffListFragment
import com.pechuro.bsuirschedule.feature.start.StartFragment
import com.pechuro.bsuirschedule.feature.update.UpdateScheduleSheet
import kotlinx.android.synthetic.main.fragment_flow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FlowFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
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
        when (childFragmentManager.currentFragment) {
            is LoadInfoFragment -> return false
        }
        if (childFragmentManager.backStackEntryCount > 0) {
            popFragment()
            return true
        }
        return false
    }

    private fun initNavigation() {
        childFragmentManager.addOnBackStackChangedListener {
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
        ScheduleItemOptionsSheet.newInstance(data).show(childFragmentManager, ScheduleItemOptionsSheet.TAG)
    }

    private fun openAddScheduleItem(schedule: Schedule) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = emptyList()
        )
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    ModifyScheduleItemFragment.newInstance(arguments),
                    ModifyScheduleItemFragment.TAG
            )
            addToBackStack(ModifyScheduleItemFragment.TAG)
        }
    }

    private fun openEditScheduleItem(schedule: Schedule, scheduleItems: List<ScheduleItem>) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = scheduleItems
        )
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    ModifyScheduleItemFragment.newInstance(arguments),
                    ModifyScheduleItemFragment.TAG
            )
            addToBackStack(ModifyScheduleItemFragment.TAG)
        }
    }

    private fun openScheduleItemDetails(data: DisplayScheduleItem) {
        when (val scheduleItem = data.scheduleItem) {
            is Lesson -> openScheduleLessonDetails(scheduleItem)
            is Exam -> openScheduleExamDetails(scheduleItem)
        }
    }

    private fun openScheduleExamDetails(exam: Exam) {

    }

    private fun openScheduleLessonDetails(lesson: Lesson) {

    }

    private fun openDatePicker(
            startDate: Date,
            endDate: Date,
            currentDate: Date
    ) {
        val arguments = DisplayScheduleDatePickerSheetArgs(startDate, endDate, currentDate)
        DisplayScheduleDatePickerSheet.newInstance(arguments).show(childFragmentManager, DisplayScheduleDatePickerSheet.TAG)
    }

    private fun openNavigationSheet() {
        NavigationSheet.newInstance().show(childFragmentManager, NavigationSheet.TAG)
    }

    private fun openLoadInfo() {
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    LoadInfoFragment.newInstance(),
                    LoadInfoFragment.TAG
            )
            addToBackStack(AddScheduleFragmentContainer.TAG)
        }
    }

    private fun openUpdateSchedules(schedules: List<Schedule>) {
        val currentFragment = childFragmentManager.currentFragment
        if (currentFragment is NavigationSheet || currentFragment is UpdateScheduleSheet) return
        UpdateScheduleSheet.newInstance(schedules.toTypedArray()).show(childFragmentManager, UpdateScheduleSheet.TAG)
    }

    private fun openAddSchedule() {
        popFragment()
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    AddScheduleFragmentContainer.newInstance(),
                    AddScheduleFragmentContainer.TAG
            )
            addToBackStack(AddScheduleFragmentContainer.TAG)
        }
    }

    private fun openDisplayScheduleOptions(schedule: Schedule) {
        DisplayScheduleOptionsSheet.newInstance(schedule).show(childFragmentManager, DisplayScheduleOptionsSheet.TAG)
    }

    private fun openViewSchedule(schedule: Schedule, skipIfOpened: Boolean = true) {
        if (skipIfOpened && viewModel.getLastOpenedSchedule() == schedule) return
        viewModel.setLastOpenedSchedule(schedule)
        setViewScheduleStartDestination(schedule)
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
        childFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    DisplayScheduleContainer.newInstance(schedule),
                    StartFragment.TAG
            )
        }
        requireView().post { updateLayoutState() }
    }

    private fun setDefaultStartDestination() {
        childFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        childFragmentManager.commit {
            replace(
                    navigationFragmentContainer.id,
                    StartFragment.newInstance(),
                    StartFragment.TAG
            )
        }
        requireView().post { updateLayoutState() }
    }

    private fun popFragment() = childFragmentManager.popBackStack()

    private fun updateLayoutState() {
        val isControlsVisible = when (childFragmentManager.currentFragment) {
            is AddScheduleFragmentContainer,
            is ModifyScheduleItemFragment,
            is StaffListFragment,
            is LoadInfoFragment -> false
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
                bottomBarAddScheduleItemButton.isVisible = true
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