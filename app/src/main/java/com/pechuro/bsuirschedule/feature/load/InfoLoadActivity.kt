package com.pechuro.bsuirschedule.feature.load

import android.content.Context
import android.content.Intent
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseActivity
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivityViewModel.Status

class InfoLoadActivity : BaseActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, InfoLoadActivity::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_info_load

    private val viewModel by lazy {
        initViewModel(InfoLoadActivityViewModel::class)
    }

    override fun onStart() {
        super.onStart()
        setStatusListeners()
    }

    private fun setStatusListeners() {
        viewModel.status.subscribe {
            when (it) {
                Status.LOADING -> handleComplete()
                Status.COMPLETE -> handleComplete()
                Status.ERROR -> handleError()
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun handleComplete() {
        /*val intent = NavigationActivity.newIntent(this)
        startActivity(intent)
        finish()*/
    }

    private fun handleError() {

    }

    private fun handleLoading() {

    }
}
