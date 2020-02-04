package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.commit
import com.pechuro.bsuirschedule.ext.currentFragment
import com.pechuro.bsuirschedule.ext.thisTag
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleEvent
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleFragmentContainer
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoEvent
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoFragment
import com.pechuro.bsuirschedule.feature.navigation.NavigationFragment
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerEvent
import com.pechuro.bsuirschedule.feature.start.StartFragmentEvent
import kotlinx.android.synthetic.main.fragment_flow.*

class FlowFragment : BaseFragment() {

    companion object {
        fun newInstance() = FlowFragment()
    }

    override val layoutId = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
    }

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.currentFragment?.onBackPressed() == true) return true
        if (childFragmentManager.backStackEntryCount > 0) {
            popFragment()
            return true
        }
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = getHomeFragment()
            addFragment(fragment, isAddToBackStack = false)
        }
        receiveEvents()
    }

    private fun getHomeFragment() = if (viewModel.isInfoLoaded()) {
        NavigationFragment.newInstance()
    } else {
        LoadInfoFragment.newInstance()
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) { event ->
            when (event) {
                is NavigationDrawerEvent.OnAddSchedule, is StartFragmentEvent.AddSchedule -> {
                    openAddScheduleFragment()
                }
                is LoadInfoEvent.OnComplete -> openNavigationFragment()
                is AddScheduleEvent.Complete -> popFragment()
                is NavigationDrawerEvent.OnOpenSettings -> {

                }
                is NavigationDrawerEvent.OnScheduleLongClick -> {

                }
                is NavigationDrawerEvent.OnTitleLongClick -> {

                }
            }
        }
    }

    private fun openAddScheduleFragment() {
        addFragment(AddScheduleFragmentContainer.newInstance(), isAddToBackStack = true)
    }

    private fun openNavigationFragment() {
        addFragment(NavigationFragment.newInstance(), isAddToBackStack = false)
    }

    private fun popFragment() = childFragmentManager.popBackStack()

    private fun addFragment(
            fragment: BaseFragment,
            isAddToBackStack: Boolean
    ) {
        childFragmentManager.commit {
            add(flowFragmentContainer.id, fragment, fragment.thisTag)
            if (isAddToBackStack) addToBackStack(fragment.thisTag)
        }
    }
}