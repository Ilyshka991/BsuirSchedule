package com.pechuro.bsuirschedule.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BackPressedHandler
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.feature.flow.FlowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), FlowFragment.ActionCallback {

    companion object {

        const val EXTRA_SCHEDULE = "EXTRA_SCHEDULE"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override val layoutId: Int = R.layout.activity_main

    private val viewModel by lazy {
        initViewModel(MainViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(EXTRA_SCHEDULE)) {
            val schedule = intent.getParcelableExtra<Schedule>(EXTRA_SCHEDULE)
            viewModel.setLastOpenedSchedule(schedule)
        }
        if (savedInstanceState == null) {
            showFlowFragment()
        }
    }

    override fun onBackPressed() {
        val flowFragment = supportFragmentManager.fragments.firstOrNull() as? BackPressedHandler
        if (flowFragment?.handleBackPressed() == true) return
        super.onBackPressed()
    }

    override fun onFlowRecreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
    }

    private fun showFlowFragment() {
        supportFragmentManager.commit {
            replace(activityHostFragment.id, FlowFragment.newInstance())
        }
    }
}