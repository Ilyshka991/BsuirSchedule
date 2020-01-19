package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.observeNonNull
import kotlinx.android.synthetic.main.fragment_drawer.*

class NavigationDrawerFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_drawer

    private val adapter = NavigationDrawerAdapter().apply {
        onScheduleClick = {
            EventBus.send(NavigationDrawerEvent.OpenSchedule(it))
        }
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationDrawerFragmentViewModel::class)
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
            EventBus.send(NavigationDrawerEvent.OpenSettings)

        }
        navDrawerAddButton.setOnClickListener {
            EventBus.send(NavigationDrawerEvent.AddSchedule)
        }
    }

    private fun observeData() {
        viewModel.schedules.observeNonNull(viewLifecycleOwner) {
            adapter.scheduleList = it
        }
    }
}