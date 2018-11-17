package com.pechuro.bsuirschedule.ui.activity.settings

import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivitySettingsBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsActivityViewModel>() {
    override val viewModel: SettingsActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(SettingsActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_settings
    override val bindingVariables: Map<Int, Any>
        get() = mapOf()
}