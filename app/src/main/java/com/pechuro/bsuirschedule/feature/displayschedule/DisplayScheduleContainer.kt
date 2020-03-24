package com.pechuro.bsuirschedule.feature.displayschedule

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleViewType
import kotlinx.android.synthetic.main.fragment_view_schedule_container.*
import java.text.SimpleDateFormat
import java.util.*

class DisplayScheduleContainer : BaseFragment() {

    companion object {

        private const val TAB_DATE_FORMAT = "EEE, dd MMM"
    }

    override val layoutId: Int = R.layout.fragment_view_schedule_container

    private val args: DisplayScheduleContainerArgs by navArgs()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleViewModel::class, owner = this).apply {
            schedule = args.schedule
        }
    }

    private val tabDateFormatter = SimpleDateFormat(TAB_DATE_FORMAT, Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Force initialize viewModel
        viewModel
        initView()
        observeData()
    }

    private fun observeData() {
        viewModel.openScheduleItemDetailsEvent.nonNull().observe(viewLifecycleOwner) {
            val event = DisplayScheduleEvent.OpenScheduleItem(it)
            EventBus.send(event)
        }
    }

    private fun initView() {
        val pagerAdapter = DisplaySchedulePagerAdapter(
                hostFragment = this,
                viewType = getViewType()
        )
        displayScheduleContainerViewPager.apply {
            adapter = pagerAdapter
            setCurrentItem(pagerAdapter.getStartPosition(), false)
        }
        if (pagerAdapter.itemCount > 1) {
            TabLayoutMediator(
                    displayScheduleContainerTabLayout,
                    displayScheduleContainerViewPager
            ) { tab, position ->
                val formattedDate = tabDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
                val weekNumber = pagerAdapter.getWeekNumberAt(position).index + 1
                val title = getString(R.string.display_schedule_tab_title, formattedDate, weekNumber)
                tab.text = title
            }.attach()
        } else {
            displayScheduleContainerTabLayout.isVisible = false
            displayScheduleContainerViewPager.isUserInputEnabled = false
        }
    }

    private fun getViewType() = when (args.schedule) {
        is Schedule.GroupClasses, is Schedule.EmployeeClasses -> DisplayScheduleViewType.DayClasses(
                startWeekNumber = viewModel.getCurrentWeekNumber()
        )
        is Schedule.EmployeeExams, is Schedule.GroupExams -> DisplayScheduleViewType.Exams
    }
}