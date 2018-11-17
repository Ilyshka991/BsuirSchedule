package com.pechuro.bsuirschedule.ui.activity.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivitySplashBinding
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class SplashActivity :
        BaseActivity<ActivitySplashBinding, SplashActivityViewModel>(), SplashNavigator {
    override val mViewModel: SplashActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(SplashActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, mViewModel))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setNavigator(this)
        if (savedInstanceState == null) mViewModel.decideNextActivity()
    }

    override fun openInfoLoadActivity() {
        val intent = InfoLoadActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    override fun openNavigationActivity() {
        val intent = NavigationActivity.newIntent(this)
        startActivity(intent)
        finish()
    }
}
