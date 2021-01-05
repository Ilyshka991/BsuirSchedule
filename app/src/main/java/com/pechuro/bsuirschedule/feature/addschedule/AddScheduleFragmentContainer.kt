package com.pechuro.bsuirschedule.feature.addschedule

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.bsuir.pechuro.bsuirschedule.R
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel.State
import kotlinx.android.synthetic.main.fragment_add_schedule_container.*

class AddScheduleFragmentContainer : BaseFragment() {

    interface ActionCallback {

        fun onAddScheduleCompleted(schedules: List<Schedule>)
    }

    companion object {

        const val TAG = "AddScheduleFragmentContainer"

        fun newInstance() = AddScheduleFragmentContainer()
    }

    override val layoutId: Int = R.layout.fragment_add_schedule_container

    private val pagerAdapter: AddScheduleContainerPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddScheduleContainerPagerAdapter(childFragmentManager)
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(AddScheduleViewModel::class, owner = this)
    }

    private var actionCallback: ActionCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
        AppAnalytics.report(AppAnalyticsEvent.AddSchedule.Opened)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    private fun initView() {
        addScheduleContainerToolbar.apply {
            setNavigationOnClickListener {
                onComplete(emptyList())
            }
        }
        addScheduleContainerViewPager.apply {
            adapter = pagerAdapter
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(addScheduleContainerTabLayout))
        }
        addScheduleContainerTabLayout.apply {
            AddScheduleContainerPagerAdapter.FragmentType.values().forEach {
                val tab = newTab().setText(getString(it.titleRes))
                addTab(tab)
            }
            addOnTabSelectedListener(onTabSelected = {
                addScheduleContainerViewPager.currentItem = it.position
            })
        }
    }

    private fun observeData() {
        viewModel.state.nonNull().observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Complete -> onComplete(state.schedules)
                is State.Loading -> setActionsEnabled(false)
                is State.Idle -> setActionsEnabled(true)
                is State.Error -> onError(state.exception)
            }
        }
    }

    private fun onComplete(schedules: List<Schedule>) {
        actionCallback?.onAddScheduleCompleted(schedules)
    }

    private fun onError(exception: Throwable) {
        AppAnalytics.report(AppAnalyticsEvent.AddSchedule.ScheduleLoadFailed(exception))
        setActionsEnabled(false)
    }

    private fun setActionsEnabled(enabled: Boolean) {
        addScheduleContainerViewPager.isSwipeEnabled = enabled
        addScheduleContainerTabLayout.setVisibleWithAlpha(enabled)
    }
}

