package com.pechuro.bsuirschedule.feature.display

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel.Event.OnScheduleItemClicked
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel.Event.OnScheduleItemLongClicked
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleViewType
import kotlinx.android.synthetic.main.fragment_display_schedule_container.*
import kotlinx.android.synthetic.main.item_schedule_actions_hint.*
import java.text.SimpleDateFormat
import java.util.*

class DisplayScheduleFragmentContainer : BaseFragment() {

    interface ActionCallback {

        fun onDisplayScheduleOpenDetails(data: DisplayScheduleItem)

        fun onDisplayScheduleOpenOptions(data: DisplayScheduleItem)

        fun onDisplaySchedulePositionChanged(position: Int)

        fun onDisplayScheduleOpenDatePicker(
                startDate: Date,
                endDate: Date,
                currentDate: Date
        )
    }

    companion object {

        const val TAG = "DisplayScheduleContainer"

        private const val BUNDLE_SCHEDULE = "BUNDLE_SCHEDULE"
        private const val TAB_DATE_FORMAT_DAY = "EEE, dd MMM"
        private const val TAB_DATE_FORMAT_WEEK = "EEEE"

        fun newInstance(schedule: Schedule) = DisplayScheduleFragmentContainer().apply {
            arguments = bundleOf(BUNDLE_SCHEDULE to schedule)
        }
    }

    override val layoutId: Int = R.layout.fragment_display_schedule_container

    private val schedule: Schedule by args(BUNDLE_SCHEDULE)

    private lateinit var viewModel: DisplayScheduleViewModel

    private var actionCallback: ActionCallback? = null

    private val tabDayDateFormatter by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(TAB_DATE_FORMAT_DAY, Locale.getDefault())
    }
    private val tabWeekDateFormatter by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(TAB_DATE_FORMAT_WEEK, Locale.getDefault())
    }
    private var tabLayoutMediator: TabLayoutMediator? = null
    private var pagerAdapter: DisplaySchedulePagerAdapter? = null
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            onPositionChanged()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = initViewModel(DisplayScheduleViewModel::class, owner = this).apply {
            schedule = this@DisplayScheduleFragmentContainer.schedule
        }
        val viewType = getViewType()
        initView(viewType)
        observeData()
    }

    override fun onResume() {
        super.onResume()
        onPositionChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyView()
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    fun requestCurrentPosition() {
        onPositionChanged()
    }

    fun setDate(date: Date = Date()) {
        val position = pagerAdapter?.getPositionForDate(date) ?: return
        setPosition(position)
    }

    fun openDatePicker() {
        val pagerAdapter = pagerAdapter ?: return
        actionCallback?.onDisplayScheduleOpenDatePicker(
                startDate = pagerAdapter.getCalendarAt(0).time,
                endDate = pagerAdapter.getCalendarAt(pagerAdapter.itemCount - 1).time,
                currentDate = pagerAdapter.getCalendarAt(displayScheduleContainerViewPager.currentItem).time
        )
    }

    private fun observeData() {
        viewModel.displayTypeData.nonNull().observe(viewLifecycleOwner) {
            val viewType = getViewType()
            if (pagerAdapter?.viewType == viewType) return@observe
            destroyView()
            initView(viewType)
        }
        viewModel.eventsData.nonNull().observe(viewLifecycleOwner) { event ->
            when (event) {
                is OnScheduleItemClicked -> actionCallback?.onDisplayScheduleOpenDetails(event.data)
                is OnScheduleItemLongClicked -> actionCallback?.onDisplayScheduleOpenOptions(event.data)
            }
        }
        viewModel.hintDisplayState.nonNull().observe(viewLifecycleOwner) { shown ->
            scheduleActionsHintParentView.isVisible = !shown
        }
    }

    private fun initView(viewType: DisplayScheduleViewType) {
        displayScheduleContainerTabLayout.removeAllTabs()

        displayScheduleContainerViewPager.registerOnPageChangeCallback(pageChangeCallback)
        val pagerAdapter = DisplaySchedulePagerAdapter(
                hostFragment = this,
                viewType = viewType,
                startWeekNumber = viewModel.currentWeekNumber
        )
        this.pagerAdapter = pagerAdapter
        displayScheduleContainerViewPager.adapter = pagerAdapter

        if (pagerAdapter.itemCount > 1) {
            initTabLayoutMediator(viewType)
            displayScheduleContainerViewPager.setCurrentItem(
                    pagerAdapter.getStartPosition(),
                    false
            )
        } else {
            displayScheduleContainerTabLayout.isVisible = false
            displayScheduleContainerViewPager.isUserInputEnabled = false
        }

        scheduleActionsHintOkButton.setSafeClickListener {
            viewModel.dismissHint()
        }
    }

    private fun initTabLayoutMediator(viewType: DisplayScheduleViewType): TabLayoutMediator {
        val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = getTabTitle(viewType, position)
        }
        return TabLayoutMediator(
                displayScheduleContainerTabLayout,
                displayScheduleContainerViewPager,
                tabConfigurationStrategy
        ).also {
            it.attach()
        }
    }

    private fun destroyView() {
        displayScheduleContainerViewPager.clearAdapter()
        pagerAdapter = null
        displayScheduleContainerViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }

    private fun getViewType() = when (schedule) {
        is Schedule.GroupClasses, is Schedule.EmployeeClasses -> {
            when (val type = viewModel.displayTypeData.value) {
                ScheduleDisplayType.DAYS -> DisplayScheduleViewType.DAY_CLASSES
                ScheduleDisplayType.WEEKS -> DisplayScheduleViewType.WEEK_CLASSES
                else -> throw IllegalArgumentException("Unknown display type: $type")
            }
        }
        is Schedule.EmployeeExams, is Schedule.GroupExams -> DisplayScheduleViewType.EXAMS
    }

    private fun onPositionChanged() {
        if (schedule is Schedule.GroupExams || schedule is Schedule.EmployeeExams) return
        val pagerAdapter = pagerAdapter ?: return
        val relativePosition = displayScheduleContainerViewPager.currentItem - pagerAdapter.getStartPosition()
        actionCallback?.onDisplaySchedulePositionChanged(relativePosition)
    }

    private fun getTabTitle(viewType: DisplayScheduleViewType, position: Int): String {
        val pagerAdapter = pagerAdapter ?: return ""
        return when (viewType) {
            DisplayScheduleViewType.DAY_CLASSES -> {
                val formattedDate = tabDayDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
                val weekNumber = pagerAdapter.getWeekNumberAt(position).index + 1
                getString(R.string.display_schedule_tab_title, formattedDate, weekNumber)
            }
            DisplayScheduleViewType.WEEK_CLASSES -> {
                tabWeekDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
            }
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    private fun setPosition(position: Int) {
        displayScheduleContainerViewPager.setCurrentItem(
                position,
                true
        )
        onPositionChanged()
        val startTab = displayScheduleContainerTabLayout.getTabAt(position)
        startTab?.select()
    }
}