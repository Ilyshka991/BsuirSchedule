package com.pechuro.bsuirschedule.ui.activity.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val isDataLoaded = viewModel.decideNextActivity()

            val intent = if (isDataLoaded) {
                NavigationActivity.newIntent(this)
            } else {
                InfoLoadActivity.newIntent(this)
            }

            startActivity(intent)
            finish()
        }
    }
}
