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
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleFragmentContainer
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
import com.pechuro.bsuirschedule.feature.update.UpdateScheduleSheetArgs
import kotlinx.android.synthetic.main.fragment_flow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FlowFragment : BaseFragment(),
        LoadInfoFragment.ActionCallback,
        AddScheduleFragmentContainer.ActionCallback,
        NavigationSheet.ActionCallback,
        DisplayScheduleFragmentContainer.ActionCallback,
        DisplayScheduleDatePickerSheet.ActionCallback,
        ScheduleItemOptionsSheet.ActionCallback {

    companion object {

        fun newInstance() = FlowFragment()
    }

    override val layoutId: Int = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
    }

    private val defaultFragmentAnimations = FragmentAnimationsResHolder(
            enter = R.animator.fragment_open_enter,
            exit = R.animator.fragment_open_exit,
            popEnter = R.animator.fragment_close_enter,
            popExit = R.animator.fragment_close_exit
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()
        if (savedInstanceState == null) setStartFragment()
        lifecycleScope.launch(Dispatchers.IO) {
            if (savedInstanceState == null && !viewModel.isInfoLoaded()) openLoadInfo()
            checkScheduleUpdates()
        }
        initViews()
    }

    override fun onResume() {
        super.onResume()
        postUpdateLayoutState()
    }

    override fun onBackPressed(): Boolean {
        val currentFragment = getCurrentFragment() as? BackPressedHandler
        if (currentFragment?.onBackPressed() == false) {
            popFragment()
            return true
        }
        return false
    }

    override fun onLoadInfoCompleted() {
        popFragment()
    }

    override fun onAddScheduleCompleted(schedules: List<Schedule>) {
        if (schedules.isEmpty()) {
            popFragment()
        } else {
            val schedule = schedules.first()
            openViewSchedule(schedule, skipIfOpened = false)
        }
    }

    override fun onNavigationSettingsClicked() {}

    override fun onNavigationAddScheduleClicked() {
        popFragment()
        openAddSchedule()
    }

    override fun onNavigationScheduleSelected(schedule: Schedule) {
        openViewSchedule(schedule)
    }

    override fun onNavigationScheduleDeleted(schedule: Schedule) {
        onScheduleDeleted(schedule)
    }

    override fun onDisplayScheduleOpenDetails(data: DisplayScheduleItem) {
        openScheduleItemDetails(data)
    }

    override fun onDisplayScheduleOpenOptions(data: DisplayScheduleItem) {
        openScheduleItemOptions(data)
    }

    override fun onDisplaySchedulePositionChanged(position: Int) {
        if (position != 0) {
            bottomBarFab.show()
        } else {
            bottomBarFab.hide()
        }
    }

    override fun onDisplayScheduleOpenDatePicker(startDate: Date, endDate: Date, currentDate: Date) {
        openDatePicker(
                startDate = startDate,
                endDate = endDate,
                currentDate = currentDate
        )
    }

    override fun onDatePickerDatePicked(date: Date) {
        setDisplayScheduleDate(date)
        popFragment()
    }

    override fun onScheduleItemOptionsEditClicked(items: List<ScheduleItem>) {
        popFragment()
        val schedule = viewModel.getLastOpenedSchedule() ?: return
        openEditScheduleItem(schedule, items)
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
            openDisplayScheduleDatePicker()
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
        val currentFragment = getCurrentFragment()
        if (currentFragment is NavigationSheet || currentFragment is UpdateScheduleSheet) return
        val args = UpdateScheduleSheetArgs(schedules)
        showDialog(UpdateScheduleSheet.newInstance(args), UpdateScheduleSheet.TAG)
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
        openPrimaryFragment(
                DisplayScheduleFragmentContainer.newInstance(schedule),
                DisplayScheduleFragmentContainer.TAG
        )
        postUpdateLayoutState()
    }

    private fun setDefaultStartFragment() {
        openPrimaryFragment(StartFragment.newInstance(), StartFragment.TAG)
        postUpdateLayoutState()
    }

    private fun postUpdateLayoutState() {
        requireView().post {
            updateLayoutState()
        }
    }

    private fun updateLayoutState() {
        val isControlsVisible = when (getCurrentFragment()) {
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
        if (fabState != null) {
            bottomBarFab.setImageDrawable(ContextCompat.getDrawable(requireContext(), fabState.iconRes))
            bottomBarFab.setSafeClickListener {
                when (fabState) {
                    FabActionState.DISPLAY_SCHEDULE_BACK -> setDisplayScheduleDate(Date())
                    FabActionState.ADD_SCHEDULE -> openAddSchedule()
                }
            }
            if (fabState == FabActionState.ADD_SCHEDULE) {
                bottomBarFab.show()
            } else {
                requestDisplayScheduleCurrentPosition()
            }
        } else {
            bottomBarFab.setOnClickListener(null)
            bottomBarFab.hide()
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

    private fun initNavigation() {
        childFragmentManager.addOnBackStackChangedListener {
            postUpdateLayoutState()
        }
    }

    private fun setDisplayScheduleDate(date: Date) {
        childFragmentManager.displayScheduleFragment?.setDate(date)
    }

    private fun requestDisplayScheduleCurrentPosition() {
        childFragmentManager.displayScheduleFragment?.requestCurrentPosition()
    }

    private fun openDisplayScheduleDatePicker() {
        childFragmentManager.displayScheduleFragment?.openDatePicker()
    }

    private fun openPrimaryFragment(
            fragment: Fragment,
            tag: String,
            animations: FragmentAnimationsResHolder = defaultFragmentAnimations
    ) {
        childFragmentManager.removeAllFragmentsImmediate()
        childFragmentManager.commit {
            animations.run {
                setCustomAnimations(enter, exit, popEnter, popExit)
            }
            replace(navigationFragmentContainer.id, fragment, tag)
            setPrimaryNavigationFragment(fragment)
        }
    }

    private fun openFragment(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean = true,
            animations: FragmentAnimationsResHolder = defaultFragmentAnimations
    ) {
        childFragmentManager.commit {
            animations.run {
                setCustomAnimations(enter, exit, popEnter, popExit)
            }
            replace(navigationFragmentContainer.id, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            } else {
                setPrimaryNavigationFragment(fragment)
            }
        }
    }

    private fun popFragment() = childFragmentManager.popBackStack()

    private fun showDialog(
            fragment: DialogFragment,
            tag: String
    ) {
        val transaction = childFragmentManager.beginTransaction().addToBackStack(tag)
        fragment.show(transaction, tag)
    }

    private fun getCurrentFragment() = childFragmentManager.currentFragment
}