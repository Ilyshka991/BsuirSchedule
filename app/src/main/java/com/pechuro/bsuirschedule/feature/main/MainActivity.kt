package com.pechuro.bsuirschedule.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.feature.main.navigationdrawer.NavigationDrawerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private val navigationDrawerCallback = object : NavigationDrawerFragment.ActionCallback {

        override fun showSchedule(schedule: Schedule) {

        }

        override fun openSetting() {

        }

        override fun addSchedule() {

        }
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationDrawer()
    }

    private fun initNavigationDrawer() {
        val fragment = navigationDrawerFragment as? NavigationDrawerFragment ?: return
        fragment.actionCallback = navigationDrawerCallback
    }
}
