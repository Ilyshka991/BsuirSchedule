package com.pechuro.bsuirschedule.feature.loadInfo

import android.content.Context
import android.os.Bundle
import android.view.View
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.getCallbackOrNull
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.ext.setVisibleWithAlpha
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel.Status
import kotlinx.android.synthetic.main.fragment_info_load.*

class LoadInfoFragment : BaseFragment() {

    interface ActionCallback {

        fun onLoadInfoCompleted()
    }

    companion object {

        const val TAG = "LoadInfoFragment"

        fun newInstance() = LoadInfoFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_info_load

    private val viewModel by lazy {
        initViewModel(LoadInfoViewModel::class)
    }

    private var actionCallback: ActionCallback? = null

    override fun handleBackPressed() = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
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
        infoLoadErrorButton.setSafeClickListener {
            viewModel.loadInfo()
        }
    }

    private fun observeData() {
        viewModel.status.nonNull().observe(this) { status ->
            when (status) {
                is Status.Complete -> handleComplete()
                is Status.Error -> handleError(status.exception)
                is Status.Loading -> handleLoading()
            }
        }
    }

    private fun handleComplete() {
        AppAnalytics.report(AppAnalyticsEvent.InfoLoad.Loaded)
        actionCallback?.onLoadInfoCompleted()
    }

    private fun handleError(exception: Throwable) {
        AppAnalytics.report(AppAnalyticsEvent.InfoLoad.Failed(exception))
        infoLoadLoaderView.setVisibleWithAlpha(false)
        infoLoadErrorParentView.setVisibleWithAlpha(true)
    }

    private fun handleLoading() {
        infoLoadLoaderView.setVisibleWithAlpha(true)
        infoLoadErrorParentView.setVisibleWithAlpha(false)
    }
}
