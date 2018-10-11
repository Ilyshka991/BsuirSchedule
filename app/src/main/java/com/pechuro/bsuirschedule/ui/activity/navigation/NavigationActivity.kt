package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.*
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.constant.ScheduleType.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.classes.ScheduleFragmentDirections.actionScheduleSelf
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentDirections.actionStartToSchedule
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation.*
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(), HasSupportFragmentInjector {

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var navController: NavController

    override val mViewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupNavigation()
        subscribeToLiveData()
    }

    override fun onSupportNavigateUp() = navigateUp(mViewDataBinding.drawerLayout, navController)

    override fun onBackPressed() {
        if (mViewDataBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        setSupportActionBar(bar)
        val toggle = ActionBarDrawerToggle(
                this, mViewDataBinding.drawerLayout, mViewDataBinding.bar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mViewDataBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mViewDataBinding.navFooter.setNavigationItemSelectedListener {
            if (it.itemId == R.id.menu_add_schedule) {
                navigate {
                    AddDialog.newInstance().show(supportFragmentManager, "add_dialog")
                }
            }
            true
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.menuItems.observe(this, Observer {
            clearMenuItems()
            addMenuItems(it)
        })
    }

    private fun clearMenuItems() {
        mViewDataBinding.navView.menu.clear()
    }

    private fun addMenuItems(items: Map<Int, List<String>>) {
        fun onClick(typeGroup: Int, menuItem: MenuItem) {
            if (menuItem.isChecked) {
                navigate {}
                return
            }

            menuItem.isCheckable = true
            menuItem.isChecked = true

            val type = when (typeGroup) {
                SCHEDULES -> if (menuItem.title.matches(Regex("[0-9]*")))
                    ScheduleType.STUDENT_CLASSES else ScheduleType.EMPLOYEE_CLASSES

                EXAMS -> if (menuItem.title.matches(Regex("[0-9]*")))
                    ScheduleType.STUDENT_EXAMS else ScheduleType.EMPLOYEE_EXAMS

                else -> throw UnsupportedOperationException("Invalid type")
            }

            val navDirection = if (navController.currentDestination?.id == R.id.scheduleFragment) {
                actionScheduleSelf(menuItem.title.toString(), type)
            } else {
                actionStartToSchedule(menuItem.title.toString(), type)
            }

            navigate {
                navController.navigate(navDirection)
            }
        }

        fun add(typeGroup: Int) {
            items[typeGroup]?.takeIf { it.isNotEmpty() }?.sorted()?.let { list ->

                val subMenu = when (typeGroup) {
                    SCHEDULES -> mViewDataBinding.navView.menu.addSubMenu(getString(R.string.schedules))
                    EXAMS -> mViewDataBinding.navView.menu.addSubMenu(getString(R.string.exams))
                    else -> throw UnsupportedOperationException("Invalid type")
                }

                list.forEach { menuItem ->
                    subMenu.add(menuItem)
                            .setOnMenuItemClickListener {
                                onClick(typeGroup, it)
                                true
                            }
                }
            }
        }

        add(SCHEDULES)
        add(EXAMS)

        mViewDataBinding.navView.menu.add("").isEnabled = false
    }

    private fun setupNavigation() {
        navController = findNavController(this, R.id.nav_host_fragment)

        setupActionBarWithNavController(this, navController, mViewDataBinding.drawerLayout)
        setupWithNavController(mViewDataBinding.navView, navController)
    }

    private fun navigate(action: () -> Unit) {
        mViewDataBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                action.invoke()
                mViewDataBinding.drawerLayout.removeDrawerListener(this)
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })
        mViewDataBinding.drawerLayout.closeDrawers()
    }
}
