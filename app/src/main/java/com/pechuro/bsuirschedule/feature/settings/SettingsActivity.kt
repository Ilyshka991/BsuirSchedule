package com.pechuro.bsuirschedule.feature.settings

import android.content.Context
import android.content.Intent
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity

class SettingsActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_settings
}