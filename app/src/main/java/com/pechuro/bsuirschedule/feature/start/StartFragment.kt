package com.pechuro.bsuirschedule.feature.start

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment

class StartFragment : BaseFragment() {

    companion object {

        const val TAG = "StartFragment"

        fun newInstance() = StartFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_start
}