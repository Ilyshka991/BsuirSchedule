package com.pechuro.bsuirschedule.feature.scheduleitemdetails

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment

class ScheduleItemDetailsFragment : BaseFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ScheduleItemDetailsViewModel::class)
    }

    override val layoutId: Int get() = R.layout.fragment_schedule_item
}