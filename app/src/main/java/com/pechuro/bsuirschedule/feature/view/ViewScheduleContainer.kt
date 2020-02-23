package com.pechuro.bsuirschedule.feature.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_view_schedule_container.*

class ViewScheduleContainer : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_view_schedule_container

    private val args: ViewScheduleContainerArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewScheduleContainerText.text = args.schedule.toString()
    }
}