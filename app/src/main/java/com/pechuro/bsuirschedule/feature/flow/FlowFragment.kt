package com.pechuro.bsuirschedule.feature.flow

import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.transaction
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoFragment
import com.pechuro.bsuirschedule.feature.navigation.NavigationFragment
import kotlinx.android.synthetic.main.fragment_flow.*

class FlowFragment : BaseFragment() {

    companion object {

        fun newInstance() = FlowFragment()
    }

    override val layoutId = R.layout.fragment_flow

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(FlowViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = if (viewModel.isInfoLoaded()) {
                NavigationFragment.newInstance()
            } else {
                LoadInfoFragment.newInstance()
            }
            childFragmentManager.transaction {
                replace(flowFragmentContainer.id, fragment)
            }
        }
    }
}