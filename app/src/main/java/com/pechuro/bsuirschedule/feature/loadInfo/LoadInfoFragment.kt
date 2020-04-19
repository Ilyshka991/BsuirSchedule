package com.pechuro.bsuirschedule.feature.loadInfo

import android.content.Context
import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.*
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

    override fun onBackPressed() = true

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
                Status.COMPLETE -> handleComplete()
                Status.ERROR -> handleError()
                Status.LOADING -> handleLoading()
            }
        }
    }

    private fun handleComplete() {
        actionCallback?.onLoadInfoCompleted()
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
