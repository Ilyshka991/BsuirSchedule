package com.pechuro.bsuirschedule.ui.activity.infoload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityInfoLoadBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class InfoLoadActivity :
        BaseActivity<ActivityInfoLoadBinding, InfoLoadActivityViewModel>() {

    override val viewModel: InfoLoadActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(InfoLoadActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_info_load
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusListeners()
    }

    private fun setStatusListeners() {
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.COMPLETE -> handleCompleteStatus()
                null -> throw UnsupportedOperationException()
            }
        })
    }

    private fun handleCompleteStatus() {
        val intent = NavigationActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, InfoLoadActivity::class.java)
    }
}
