package com.pechuro.bsuirschedule.feature.loadinfo

import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.ext.setVisibleWithAlpha
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel.Status
import kotlinx.android.synthetic.main.fragment_info_load.*

class LoadInfoFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_info_load

    private val viewModel by lazy {
        initViewModel(LoadInfoViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        infoLoadErrorButton.setSafeClickListener {
            viewModel.loadInfo()
        }
    }

    private fun observeData() {
        viewModel.status.observeNonNull(this) {
            when (it) {
                Status.COMPLETE -> handleComplete()
                Status.ERROR -> handleError()
                Status.LOADING -> handleLoading()
            }
        }
    }

    private fun handleComplete() {
        EventBus.send(LoadInfoCompleteEvent)
    }

    private fun handleError() {
        infoLoadLoaderView.setVisibleWithAlpha(false)
        infoLoadErrorParentView.setVisibleWithAlpha(true)
    }

    private fun handleLoading() {
        infoLoadLoaderView.setVisibleWithAlpha(true)
        infoLoadErrorParentView.setVisibleWithAlpha(false)
    }
}
