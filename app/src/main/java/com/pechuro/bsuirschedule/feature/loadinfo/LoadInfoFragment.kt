package com.pechuro.bsuirschedule.feature.loadinfo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel.Status
import kotlinx.android.synthetic.main.fragment_info_load.*

class LoadInfoFragment : BaseFragment() {

    companion object {

        fun newInstance() = LoadInfoFragment()
    }

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
        infoLoadErrorButton.setOnClickListener {
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
        EventBus.send(LoadInfoEvent.OnComplete)
    }

    private fun handleError() {
        infoLoadProgressParentView.isVisible = false
        infoLoadErrorParentView.isVisible = true
    }

    private fun handleLoading() {
        infoLoadProgressParentView.isVisible = true
        infoLoadErrorParentView.isVisible = false
    }
}
