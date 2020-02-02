package com.pechuro.bsuirschedule.feature.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragmentActivity
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleContainer
import com.pechuro.bsuirschedule.feature.add.addschedule.AddScheduleEvent
import kotlinx.android.synthetic.main.activity_container.*

class AddScheduleActivity : BaseFragmentActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, AddScheduleActivity::class.java)
    }

    override val layoutId: Int = R.layout.activity_container

    override val containerId: Int by lazy(LazyThreadSafetyMode.NONE) {
        fragmentContainer.id
    }

    override fun getHomeFragment() = AddScheduleContainer.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveEvents()
    }

    private fun receiveEvents() {
        EventBus.receive<AddScheduleEvent>(lifecycleScope) {
            when (it) {
                is AddScheduleEvent.Complete -> finish()
            }
        }
    }
}
