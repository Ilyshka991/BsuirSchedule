package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.observeNonNull
import kotlinx.android.synthetic.main.fragment_drawer.*

class NavigationDrawerFragment : BaseFragment() {

    interface ActionCallback {

        fun showSchedule(schedule: Schedule)

        fun openSetting()

        fun addSchedule()
    }

    override val layoutId: Int = R.layout.fragment_drawer

    var actionCallback: ActionCallback? = null

    private val adapter = NavigationDrawerAdapter().apply {
        onScheduleClick = {
            actionCallback?.showSchedule(it)
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
            actionCallback?.openSetting()
        }
        navDrawerAddButton.setOnClickListener {
            actionCallback?.addSchedule()
        }
    }

    private fun observeData() {
        viewModel.schedules.observeNonNull(viewLifecycleOwner) {
            adapter.scheduleList = it
        }
    }
}