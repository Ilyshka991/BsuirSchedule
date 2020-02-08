package com.pechuro.bsuirschedule.feature.navigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.fragment_navigation_sheet.*

class NavigationSheet : BaseBottomSheetDialog() {

    override val layoutId: Int = R.layout.fragment_navigation_sheet

    private val adapterActionCallback = object : NavigationDrawerAdapter.ActionCallback {

        override fun onScheduleClicked(schedule: Schedule) {
            EventBus.send(NavigationSheetEvent.OnScheduleClick(schedule))
        }

        override fun onScheduleLongClicked(schedule: Schedule) {
            EventBus.send(NavigationSheetEvent.OnScheduleLongClick(schedule))
        }

        override fun onTitleClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationSheetEvent.OnTitleClick(scheduleType))
        }

        override fun onTitleLongClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationSheetEvent.OnTitleLongClick(scheduleType))
        }
    }
    private val adapter = NavigationDrawerAdapter().apply {
        actionCallback = adapterActionCallback
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationSheetViewModel::class)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        navigationSheetItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@NavigationSheet.adapter
            itemAnimator = null
        }
        navigationSheetSettingButton.setSafeClickListener {
            EventBus.send(NavigationSheetEvent.OnOpenSettings)

        }
        navigationSheetAddButton.setSafeClickListener {
            EventBus.send(NavigationSheetEvent.OnAddSchedule)
        }
    }

    private fun observeData() {
        viewModel.schedules.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}