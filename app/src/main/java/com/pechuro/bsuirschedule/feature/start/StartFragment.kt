package com.pechuro.bsuirschedule.feature.start

import android.os.Bundle
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : BaseFragment() {

    companion object {

        fun newInstance() = StartFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        startFragmentAddFab.setOnClickListener {
            EventBus.send(StartFragmentEvent.AddSchedule)
        }
    }
}