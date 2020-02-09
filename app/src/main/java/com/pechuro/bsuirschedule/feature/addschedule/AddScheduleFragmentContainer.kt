package com.pechuro.bsuirschedule.feature.addschedule

import android.os.Bundle
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.addOnTabSelectedListener
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setVisibleWithAlpha
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel.State
import kotlinx.android.synthetic.main.fragment_add_schedule_container.*

class AddScheduleFragmentContainer : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_add_schedule_container

    private val pagerAdapter: AddScheduleContainerPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddScheduleContainerPagerAdapter(childFragmentManager)
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(AddScheduleViewModel::class, owner = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        addScheduleContainerToolbar.apply {
            setNavigationOnClickListener {
                onComplete()
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
        viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
            when (state) {
                is State.Complete -> onComplete()
                is State.Loading -> setActionsEnabled(false)
                is State.Idle -> setActionsEnabled(true)
                is State.Error -> setActionsEnabled(false)
            }
        }
    }

    private fun onComplete() {
        EventBus.send(AddScheduleCompleteEvent)
    }

    private fun setActionsEnabled(enabled: Boolean) {
        addScheduleContainerViewPager.isSwipeEnabled = enabled
        addScheduleContainerTabLayout.setVisibleWithAlpha(enabled)
    }
}

