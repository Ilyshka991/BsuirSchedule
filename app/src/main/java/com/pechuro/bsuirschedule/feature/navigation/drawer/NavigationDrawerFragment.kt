package com.pechuro.bsuirschedule.feature.navigation.drawer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.observeNonNull
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*

class NavigationDrawerFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_navigation_drawer

    private val adapterActionCallback = object : NavigationDrawerAdapter.ActionCallback {

        override fun onScheduleClicked(schedule: Schedule) {
            EventBus.send(NavigationDrawerEvent.OnScheduleClick(schedule))
        }

        override fun onScheduleLongClicked(schedule: Schedule) {
            EventBus.send(NavigationDrawerEvent.OnScheduleLongClick(schedule))
        }

        override fun onTitleClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationDrawerEvent.OnTitleClick(scheduleType))
        }

        override fun onTitleLongClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationDrawerEvent.OnTitleLongClick(scheduleType))
        }
    }
    private val adapter = NavigationDrawerAdapter().apply {
        actionCallback = adapterActionCallback
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationDrawerViewModel::class)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        navDrawerItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@NavigationDrawerFragment.adapter
            itemAnimator = null
        }
        navDrawerSettingButton.setOnClickListener {
            EventBus.send(NavigationDrawerEvent.OnOpenSettings)

        }
        navDrawerAddButton.setOnClickListener {
            EventBus.send(NavigationDrawerEvent.OnAddSchedule)
        }
    }

    private fun observeData() {
        viewModel.schedules.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}