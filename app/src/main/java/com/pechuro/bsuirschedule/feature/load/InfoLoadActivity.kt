package com.pechuro.bsuirschedule.feature.load

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivityViewModel.Status
import com.pechuro.bsuirschedule.feature.main.MainActivity
import kotlinx.android.synthetic.main.activity_info_load.*

class InfoLoadActivity : BaseActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, InfoLoadActivity::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_info_load

    private val viewModel by lazy {
        initViewModel(InfoLoadActivityViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
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
