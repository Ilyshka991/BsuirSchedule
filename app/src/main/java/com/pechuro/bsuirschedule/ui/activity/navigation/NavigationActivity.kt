package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constant.SharedPrefConstants.SCHEDULE_NAME
import com.pechuro.bsuirschedule.constant.SharedPrefConstants.SCHEDULE_TYPE
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragment
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragment
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ScheduleInformation
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation.*
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(), HasSupportFragmentInjector {

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

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
        homeFragment()
        subscribeToLiveData()
    }

    fun homeFragment() {
        val name = defaultSharedPreferences.getString(SCHEDULE_NAME, "")
        val type = defaultSharedPreferences.getInt(SCHEDULE_TYPE, -1)

        val fragment = if (name.isNullOrEmpty() || type == -1) {
            StartFragment.newInstance()
        } else {
            val argInfo = ScheduleInformation(name!!, type)
            when (type) {
                STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(argInfo)
                STUDENT_EXAMS, EMPLOYEE_EXAMS -> ListFragment.newInstance(argInfo)
                else -> throw UnsupportedOperationException("Invalid type")
            }
        }

        supportFragmentManager.transaction {
            replace(mViewDataBinding.navHostFragment.id, fragment)
        }
    }

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

    private fun clearMenuItems() = mViewDataBinding.navView.menu.clear()

    private fun addMenuItems(items: Map<Int, List<String>>) {
        fun onClick(typeGroup: Int, menuItem: MenuItem) {
            if (menuItem.isChecked) {
                navigate {}
                return
            }

            menuItem.isCheckable = true
            menuItem.isChecked = true

            val name = menuItem.title.toString()

            val type = when (typeGroup) {
                SCHEDULES -> if (name.matches(Regex("[0-9]*")))
                    STUDENT_CLASSES else EMPLOYEE_CLASSES

                EXAMS -> if (name.matches(Regex("[0-9]*")))
                    STUDENT_EXAMS else EMPLOYEE_EXAMS

                else -> throw UnsupportedOperationException("Invalid type")
            }

            val argInfo = ScheduleInformation(name, type)
            val fragment = when (typeGroup) {
                SCHEDULES -> ClassesFragment.newInstance(argInfo)
                EXAMS -> ListFragment.newInstance(argInfo)
                else -> throw UnsupportedOperationException("Invalid type")
            }

            navigate {
                defaultSharedPreferences.edit {
                    putString(SCHEDULE_NAME, name)
                    putInt(SCHEDULE_TYPE, type)
                }

                supportFragmentManager.transaction {
                    replace(mViewDataBinding.navHostFragment.id, fragment)
                }
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
