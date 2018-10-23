package com.pechuro.bsuirschedule.ui.activity.infoload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityInfoLoadBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class InfoLoadActivity :
        BaseActivity<ActivityInfoLoadBinding, InfoLoadActivityViewModel>(), InfoLoadNavigator {

    companion object {
        fun newIntent(context: Context) = Intent(context, InfoLoadActivity::class.java)
    }

    override val mViewModel: InfoLoadActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(InfoLoadActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_info_load
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCallback()
        setListeners()
    }

    override fun onSuccess() {
        val intent = NavigationActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun setCallback() {
        mViewModel.navigator = this
    }

    private fun setListeners() {
        mViewDataBinding.retryButton.setOnClickListener {
            mViewModel.onRetryClick()
        }
    }
}
