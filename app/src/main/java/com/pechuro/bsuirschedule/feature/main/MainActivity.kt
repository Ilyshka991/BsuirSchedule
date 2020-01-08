package com.pechuro.bsuirschedule.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.transaction
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleDialog
import com.pechuro.bsuirschedule.feature.main.navigationdrawer.NavigationDrawerFragment
import com.pechuro.bsuirschedule.feature.main.start.StartFragment
import com.pechuro.bsuirschedule.widget.listeners.DrawerListener
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
            openAddSchedule()
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    private val fragmentHostId by lazy(LazyThreadSafetyMode.NONE) {
        fragmentHostView.id
    }
    private val isNavigationDrawerOpen: Boolean
        get() = navigationDrawerParentView.isDrawerOpen(navigationDrawerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationDrawer()
        if (savedInstanceState == null) {
            navigate {
                val fragment = getHomeFragment()
                replace(fragmentHostId, fragment)
            }
        }
    }

    override fun onBackPressed() {
        if (isNavigationDrawerOpen) {
            closeNavigationDrawer()
            return
        }
        super.onBackPressed()
    }

    private fun initNavigationDrawer() {
        val fragment = navigationDrawerFragment as? NavigationDrawerFragment ?: return
        fragment.actionCallback = navigationDrawerCallback
    }

    private fun closeNavigationDrawer() {
        navigationDrawerParentView.closeDrawers()
    }

    private inline fun navigate(crossinline action: FragmentTransaction. () -> Unit) {
        if (isNavigationDrawerOpen) {
            navigationDrawerParentView.addDrawerListener(object : DrawerListener {
                override fun onDrawerClosed(drawerView: View) {
                    supportFragmentManager.transaction(body = action)
                    navigationDrawerParentView.removeDrawerListener(this)
                }
            })
            closeNavigationDrawer()
        } else {
            supportFragmentManager.transaction(body = action)
        }
    }

    private fun getHomeFragment(): BaseFragment {
        return StartFragment.newInstance().apply {
            actionCallback = object : StartFragment.Callback {
                override fun addSchedule() {
                    openAddSchedule()
                }
            }
        }
    }

    private fun openAddSchedule() {
        navigate {
            AddScheduleDialog.newInstance().show(supportFragmentManager, AddScheduleDialog.TAG)
        }
    }
}
