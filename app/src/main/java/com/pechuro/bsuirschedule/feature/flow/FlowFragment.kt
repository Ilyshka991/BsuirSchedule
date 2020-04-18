package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BackPressedHandler
import com.pechuro.bsuirschedule.common.FragmentAnimationsResHolder
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.currentFragment
import com.pechuro.bsuirschedule.ext.displayScheduleFragment
import com.pechuro.bsuirschedule.ext.removeAllFragmentsImmediate
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleFragmentContainer
import com.pechuro.bsuirschedule.feature.datepicker.DisplayScheduleDatePickerSheet
import com.pechuro.bsuirschedule.feature.datepicker.DisplayScheduleDatePickerSheetArgs
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleContainer
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.itemoptions.ScheduleItemOptionsSheet
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoFragment
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragment
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragmentArgs
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheet
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

    private val defaultFragmentAnimations = FragmentAnimationsResHolder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()
        setStartFragment()
        lifecycleScope.launch(Dispatchers.IO) {
            if (savedInstanceState == null && !viewModel.isInfoLoaded()) openLoadInfo()
            checkScheduleUpdates()
        }
        initViews()
        receiveEvents()
    }

    override fun onResume() {
        super.onResume()
        updateLayoutState()
    }

    override fun onBackPressed(): Boolean {
        val currentFragment = childFragmentManager.currentFragment as? BackPressedHandler
        if (currentFragment?.onBackPressed() == false) {
            popFragment()
            return true
        }
        return false
    }

    private fun initNavigation() {
        childFragmentManager.addOnBackStackChangedListener {
            updateLayoutState()
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
            childFragmentManager.displayScheduleFragment?.openDatePicker()
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
        /*EventBus.receive<BaseEvent>(eventCoroutineScope) { event ->
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
        }*/
    }

    private fun openScheduleItemOptions(data: DisplayScheduleItem) {
        showDialog(ScheduleItemOptionsSheet.newInstance(data), ScheduleItemOptionsSheet.TAG)
    }

    private fun openAddScheduleItem(schedule: Schedule) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = emptyList()
        )
        openFragment(ModifyScheduleItemFragment.newInstance(arguments), ModifyScheduleItemFragment.TAG)
    }

    private fun openEditScheduleItem(schedule: Schedule, scheduleItems: List<ScheduleItem>) {
        val arguments = ModifyScheduleItemFragmentArgs(
                schedule = schedule,
                items = scheduleItems
        )
        openFragment(ModifyScheduleItemFragment.newInstance(arguments), ModifyScheduleItemFragment.TAG)
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
        showDialog(DisplayScheduleDatePickerSheet.newInstance(arguments), DisplayScheduleDatePickerSheet.TAG)
    }

    private fun openNavigationSheet() {
        showDialog(NavigationSheet.newInstance(), NavigationSheet.TAG)
    }

    private fun openLoadInfo() {
        openFragment(LoadInfoFragment.newInstance(), LoadInfoFragment.TAG)
    }

    private fun openUpdateSchedules(schedules: List<Schedule>) {
        val currentFragment = childFragmentManager.currentFragment
        if (currentFragment is NavigationSheet || currentFragment is UpdateScheduleSheet) return
        showDialog(UpdateScheduleSheet.newInstance(schedules.toTypedArray()), UpdateScheduleSheet.TAG)
    }

    private fun openAddSchedule() {
        openFragment(AddScheduleFragmentContainer.newInstance(), AddScheduleFragmentContainer.TAG)
    }

    private fun openDisplayScheduleOptions(schedule: Schedule) {
        showDialog(DisplayScheduleOptionsSheet.newInstance(schedule), DisplayScheduleOptionsSheet.TAG)
    }

    private fun openViewSchedule(schedule: Schedule, skipIfOpened: Boolean = true) {
        if (skipIfOpened && viewModel.getLastOpenedSchedule() == schedule) return
        viewModel.setLastOpenedSchedule(schedule)
        setViewScheduleStartFragment(schedule)
    }

    private fun onScheduleDeleted(schedule: Schedule) {
        if (viewModel.getLastOpenedSchedule() == schedule) {
            viewModel.setLastOpenedSchedule(null)
            setDefaultStartFragment()
        }
    }

    private fun setStartFragment() {
        val lastOpenedSchedule = viewModel.getLastOpenedSchedule()
        if (lastOpenedSchedule == null) {
            setDefaultStartFragment()
        } else {
            setViewScheduleStartFragment(lastOpenedSchedule)
        }
    }

    private fun setViewScheduleStartFragment(schedule: Schedule) {
        childFragmentManager.removeAllFragmentsImmediate()
        openFragment(
                DisplayScheduleContainer.newInstance(schedule),
                DisplayScheduleContainer.TAG,
                addToBackStack = false
        )
        updateLayoutState()
    }

    private fun setDefaultStartFragment() {
        childFragmentManager.removeAllFragmentsImmediate()
        openFragment(StartFragment.newInstance(), StartFragment.TAG, addToBackStack = false)
        updateLayoutState()
    }

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
                    FabActionState.DISPLAY_SCHEDULE_BACK -> setDisplayScheduleFirstDay()
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

    private fun setDisplayScheduleFirstDay() {
        childFragmentManager.displayScheduleFragment?.setFirstDay()
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

    private fun openFragment(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean = true,
            animations: FragmentAnimationsResHolder = defaultFragmentAnimations
    ) {
        childFragmentManager.commit {
            replace(navigationFragmentContainer.id, fragment, tag)
            if (addToBackStack) addToBackStack(tag)
            animations.run {
                setCustomAnimations(enter, exit, popEnter, popExit)
            }
        }
    }

    private fun popFragment() = childFragmentManager.popBackStack()

    private fun showDialog(
            fragment: DialogFragment,
            tag: String
    ) {
        fragment.show(childFragmentManager, tag)
    }
}