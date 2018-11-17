package com.pechuro.bsuirschedule.ui.activity.splash

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivitySplashBinding
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class SplashActivity :
        BaseActivity<ActivitySplashBinding, SplashActivityViewModel>() {
    override val viewModel: SplashActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(SplashActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEventListeners()
        if (savedInstanceState == null) viewModel.decideNextActivity()
    }

    private fun setEventListeners() {
        viewModel.command.observe(this, Observer {
            val intent = when (it) {
                is OpenInfoLoadActivity -> {
                    InfoLoadActivity.newIntent(this)
                }
                is OpenNavigationActivity -> {
                    NavigationActivity.newIntent(this)
                }
            }
            startActivity(intent)
            finish()
        })
    }
}
