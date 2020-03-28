package com.pechuro.bsuirschedule.feature.displayschedule

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.clearAdapter
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleViewType
import com.pechuro.bsuirschedule.feature.flow.FlowFragmentEvent
import kotlinx.android.synthetic.main.fragment_view_schedule_container.*
import java.text.SimpleDateFormat
import java.util.*

class DisplayScheduleContainer : BaseFragment() {

    companion object {

        private const val TAB_DATE_FORMAT = "EEE, dd MMM"
    }

    override val layoutId: Int = R.layout.fragment_view_schedule_container

    private val args: DisplayScheduleContainerArgs by navArgs()

    private lateinit var viewModel: DisplayScheduleViewModel

    private val tabDateFormatter = SimpleDateFormat(TAB_DATE_FORMAT, Locale.getDefault())

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
        initView()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        onPositionChanged()
    }

    override fun onDestroyView() {
        displayScheduleContainerViewPager.clearAdapter()
        displayScheduleContainerViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        tabLayoutMediator?.detach()
        super.onDestroyView()
    }

    private fun observeData() {
        EventBus.receive<FlowFragmentEvent>(lifecycleScope) {
            when (it) {
                is FlowFragmentEvent.DisplayScheduleLessonsSetFirstDay -> setFirstDay()
                is FlowFragmentEvent.DisplayScheduleExamsAddExam -> {
                    //TODO: Implement
                }
            }
        }
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
        this.pagerAdapter = pagerAdapter
        displayScheduleContainerViewPager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
            setCurrentItem(pagerAdapter.getStartPosition(), false)
        }
        if (pagerAdapter.itemCount > 1) {
            tabLayoutMediator = TabLayoutMediator(
                    displayScheduleContainerTabLayout,
                    displayScheduleContainerViewPager
            ) { tab, position ->
                val formattedDate = tabDateFormatter.format(pagerAdapter.getCalendarAt(position).time)
                val weekNumber = pagerAdapter.getWeekNumberAt(position).index + 1
                val title = getString(R.string.display_schedule_tab_title, formattedDate, weekNumber)
                tab.text = title
            }.apply { attach() }
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

    private fun onPositionChanged() {
        if (args.schedule is Schedule.GroupExams || args.schedule is Schedule.EmployeeExams) return
        val pagerAdapter = pagerAdapter ?: return
        val relativePosition = displayScheduleContainerViewPager.currentItem - pagerAdapter.getStartPosition()
        EventBus.send(DisplayScheduleEvent.OnPositionChanged(relativePosition))
    }

    private fun setFirstDay() {
        val pagerAdapter = pagerAdapter ?: return
        displayScheduleContainerViewPager.setCurrentItem(
                pagerAdapter.getStartPosition(),
                true
        )
    }
}