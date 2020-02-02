package com.pechuro.bsuirschedule.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.common.base.BaseFragmentActivity
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleContainerDialog
import com.pechuro.bsuirschedule.feature.main.navigationdrawer.NavigationDrawerEvent
import com.pechuro.bsuirschedule.feature.main.start.StartFragment
import com.pechuro.bsuirschedule.feature.main.start.StartFragmentEvent
import com.pechuro.bsuirschedule.widget.DefaultDrawerListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseFragmentActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override val layoutId: Int = R.layout.activity_main

    override val containerId: Int by lazy(LazyThreadSafetyMode.NONE) {
        fragmentHostView.id
    }

    private val isNavigationDrawerOpen: Boolean
        get() = navigationDrawerParentView.isDrawerOpen(navigationDrawerView)

    override fun getHomeFragment(): BaseFragment {
        return StartFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveEvents()
    }

    override fun onBackPressed() {
        if (isNavigationDrawerOpen) {
            closeNavigationDrawer()
            return
        }
        super.onBackPressed()
    }

    private fun receiveEvents() {
        EventBus.receive<BaseEvent>(lifecycleScope) {
            when (it) {
                is StartFragmentEvent.AddSchedule, is NavigationDrawerEvent.OnAddSchedule -> openAddSchedule()
                is NavigationDrawerEvent.OnOpenSettings -> {

                }
                is NavigationDrawerEvent.OnScheduleClick -> {

                }
            }
        }
    }

    private fun openAddSchedule() {
        doOnNavDrawerClosed {
            showDialog(AddScheduleContainerDialog.newInstance(), AddScheduleContainerDialog.TAG)
        }
    }

    private inline fun doOnNavDrawerClosed(crossinline action: () -> Unit) {
        if (isNavigationDrawerOpen) {
            navigationDrawerParentView.addDrawerListener(object : DefaultDrawerListener {
                override fun onDrawerClosed(drawerView: View) {
                    action()
                    navigationDrawerParentView.removeDrawerListener(this)
                }
            })
            closeNavigationDrawer()
        } else {
            action()
        }
    }

    private fun closeNavigationDrawer() {
        navigationDrawerParentView.closeDrawers()
    }
}
