package com.pechuro.bsuirschedule.ui.activity.navigation

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
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.*
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.constant.ScheduleType.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.classes.ScheduleFragmentDirections.actionScheduleSelf
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentDirections.actionStartToSchedule
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation.*
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: ActivityNavigationBinding
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
        binding = mViewDataBinding
        setupView()
        setupNavigation()
        subscribeToLiveData()
    }

    override fun onSupportNavigateUp() = navigateUp(binding.drawerLayout, navController)

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        setSupportActionBar(bar)
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.bar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun subscribeToLiveData() {
        mViewModel.menuItems.observe(this, Observer { addMenuItems(it) })
    }

    private fun addMenuItems(items: Map<Int, List<String>>) {
        fun onClick(typeGroup: Int, menuItem: MenuItem) {
            if (menuItem.isChecked) {
                navigate(null)
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

            navigate(navDirection)
        }

        fun add(typeGroup: Int) {
            items[typeGroup]?.takeIf { it.isNotEmpty() }?.sorted()?.let { list ->

                val subMenu = when (typeGroup) {
                    SCHEDULES -> binding.navView.menu.addSubMenu("Schedules")
                    EXAMS -> binding.navView.menu.addSubMenu("Exams")
                    else -> throw UnsupportedOperationException("Invalid type")
                }

                list.forEach { menuItem ->
                    subMenu.add(menuItem).setOnMenuItemClickListener {
                        onClick(typeGroup, it)
                        true
                    }
                }
            }
        }

        add(SCHEDULES)
        add(EXAMS)
    }

    private fun setupNavigation() {
        navController = findNavController(this, R.id.nav_host_fragment)

        setupActionBarWithNavController(this, navController, binding.drawerLayout)
        setupWithNavController(binding.navView, navController)
    }

    private fun navigate(navDirection: NavDirections?) {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                if (navDirection != null) navController.navigate(navDirection)
                binding.drawerLayout.removeDrawerListener(this)
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })
        binding.drawerLayout.closeDrawers()
    }
}
