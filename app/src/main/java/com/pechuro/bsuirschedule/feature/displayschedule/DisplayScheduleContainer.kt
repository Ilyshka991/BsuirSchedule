package com.pechuro.bsuirschedule.feature.displayschedule

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.ext.clearAdapter
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleViewType
import com.pechuro.bsuirschedule.feature.displayscheduledatepicker.ScheduleDatePickedEvent
import com.pechuro.bsuirschedule.feature.flow.FlowFragmentEvent
import kotlinx.android.synthetic.main.fragment_view_schedule_container.*
import java.text.SimpleDateFormat
import java.util.*

class DisplayScheduleContainer : BaseFragment() {

    companion object {

        private const val TAB_DATE_FORMAT_DAY = "EEE, dd MMM"
        private const val TAB_DATE_FORMAT_WEEK = "EEEE"
    }

    override val layoutId: Int = R.layout.fragment_view_schedule_container

    private val args: DisplayScheduleContainerArgs by navArgs()

    private lateinit var viewModel: DisplayScheduleViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = initViewModel(DisplayScheduleViewModel::class, owner = this).apply {
            schedule = args.schedule
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

    private fun observeData() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
                is FlowFragmentEvent.DisplayScheduleSetToday -> setFirstDay()
                is FlowFragmentEvent.DisplayScheduleGoToDate -> openDatePicker()
                is ScheduleDatePickedEvent -> setDate(event.date)
                is FlowFragmentEvent.DisplayScheduleAddItem -> {
                    //TODO: Implement
                }
            }
        }
        viewModel.displayTypeData.nonNull().observe(viewLifecycleOwner) {
            val viewType = getViewType()
            if (pagerAdapter?.viewType == viewType) return@observe
            destroyView()
            initView(viewType)
        }
    }

    private fun initView(viewType: DisplayScheduleViewType) {
        displayScheduleContainerTabLayout.removeAllTabs()

        displayScheduleContainerViewPager.registerOnPageChangeCallback(pageChangeCallback)
        val pagerAdapter = DisplaySchedulePagerAdapter(
                hostFragment = this,
                viewType = viewType
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

    private fun getViewType() = when (args.schedule) {
        is Schedule.GroupClasses, is Schedule.EmployeeClasses -> {
            when (val type = viewModel.displayTypeData.value) {
                ScheduleDisplayType.DAYS -> DisplayScheduleViewType.DayClasses(
                        startWeekNumber = viewModel.currentWeekNumber
                )
                ScheduleDisplayType.WEEKS -> DisplayScheduleViewType.WeekClasses
                else -> throw IllegalArgumentException("Unknown display type: $type")
            }
        }
        is Schedule.EmployeeExams, is Schedule.GroupExams -> DisplayScheduleViewType.Exams
    }

    private fun onPositionChanged() {
        if (args.schedule is Schedule.GroupExams || args.schedule is Schedule.EmployeeExams) return
        val pagerAdapter = pagerAdapter ?: return
        val relativePosition = displayScheduleContainerViewPager.currentItem - pagerAdapter.getStartPosition()
        EventBus.send(DisplayScheduleEvent.OnPositionChanged(relativePosition))
    }

    private fun getTabTitle(viewType: DisplayScheduleViewType, position: Int): String {
        val pagerAdapter = pagerAdapter ?: return ""
        return when (viewType) {
            is DisplayScheduleViewType.DayClasses -> {
                val formattedDate = tabDayDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
                val weekNumber = pagerAdapter.getWeekNumberAt(position).index + 1
                getString(R.string.display_schedule_tab_title, formattedDate, weekNumber)
            }
            is DisplayScheduleViewType.WeekClasses -> {
                tabWeekDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
            }
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    private fun openDatePicker() {
        val pagerAdapter = pagerAdapter ?: return
        EventBus.send(DisplayScheduleEvent.OpenDatePicker(
                startDate = pagerAdapter.getCalendarAt(0).time,
                endDate = pagerAdapter.getCalendarAt(pagerAdapter.itemCount - 1).time,
                currentDate = pagerAdapter.getCalendarAt(displayScheduleContainerViewPager.currentItem).time
        ))
    }

    private fun setFirstDay() {
        val startPosition = pagerAdapter?.getStartPosition() ?: return
        setPosition(startPosition)
    }

    private fun setDate(date: Date) {
        val position = pagerAdapter?.getPositionForDate(date) ?: return
        setPosition(position)
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