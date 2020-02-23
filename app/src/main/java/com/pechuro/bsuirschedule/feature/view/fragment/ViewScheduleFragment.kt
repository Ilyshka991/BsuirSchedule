package com.pechuro.bsuirschedule.feature.view.fragment

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.feature.addschedule.fragment.AddScheduleFragment

class ViewScheduleFragment : BaseFragment() {

    companion object {

        fun newInstance() = AddScheduleFragment()
    }

    override val layoutId: Int = R.layout.fragment_view_schedule

}