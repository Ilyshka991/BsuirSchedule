package com.pechuro.bsuirschedule.feature.scheduleitemdetails

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment

class ScheduleItemFragment : BaseFragment() {

    private val viewModel by lazy {
        initViewModel(ScheduleItemViewModel::class)
    }

    override val layoutId: Int get() = R.layout.fragment_schedule_item
}