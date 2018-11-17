package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SCHEDULE_NAME
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SCHEDULE_TYPE
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemAdapter
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.custom.OnSwipeTouchListener
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.bottomsheet.OptionsFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragment
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_navigation.*
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(),
        HasSupportFragmentInjector, NavItemAdapter.NavCallback, AddDialog.AddDialogCallback,
        OptionsFragment.ScheduleOptionsCallback, NavNavigator {
    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mNavAdapter: NavItemAdapter

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override val viewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setCallback()
        setListeners()
        if (savedInstanceState == null) {
            homeFragment()
        }
        setupBottomBar()
        subscribeToLiveData()
    }

    override fun onBackPressed() {
        if (viewDataBinding.drawerLayout
                        .isDrawerOpen(GravityCompat.START)) {
            viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onRequestUpdate(schedule: Schedule) {
        Toast.makeText(this, "${schedule.name} - ${schedule.type} need update", Toast.LENGTH_LONG).show()

        viewModel.updateSchedule(schedule)
    }

    override fun onScheduleUpdated(name: String, type: Int) {
        Snackbar.make(viewDataBinding.contentLayout, "$name - $type updated!!!!!!!!!!", Snackbar.LENGTH_SHORT).show()

        val currentName = getCurrentScheduleName()
        val currentType = getCurrentScheduleType()
        if (name == currentName && type == currentType) {
            homeFragment()
        }
    }

    override fun onScheduleUpdateFail(name: String, type: Int) {
        Snackbar.make(viewDataBinding.contentLayout, "$name - $type update fail !!!!!!!!!!", Snackbar.LENGTH_SHORT).show()
    }

    override fun onNavigate(info: ScheduleInformation) {
        val argInfo = ScheduleInformation(info.name, info.type)
        val fragment = when (info.type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(argInfo)
            STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(argInfo)
            else -> throw IllegalStateException("Invalid type")
        }
        navigate {
            supportFragmentManager.transaction {
                replace(viewDataBinding.navHostFragment.id, fragment)

                defaultSharedPreferences.edit {
                    putString(SCHEDULE_NAME, info.name)
                    putInt(SCHEDULE_TYPE, info.type)
                }

                setupBottomBar()
            }
        }
    }

    override fun onLongClick(info: ScheduleInformation) {
        viewModel.deleteSchedule(info.name, info.type)
    }

    override fun onAddDialogDismiss() {
        homeFragment()
        setupBottomBar()
    }

    override fun addLesson() {
        val name = getCurrentScheduleName()!!
        val type = getCurrentScheduleType()

        val intent = EditLessonActivity.newIntent(this, name, type)
        startActivity(intent)
    }

    private fun subscribeToLiveData() {
        viewModel.menuItems.observe(this, Observer {
            mNavAdapter.setItems(it)
        })
    }

    private fun setupView() {
        setSupportActionBar(bar)

        mLayoutManager.orientation = RecyclerView.VERTICAL
        viewDataBinding.navItemList.layoutManager = mLayoutManager
        viewDataBinding.navItemList.adapter = mNavAdapter
    }

    private fun setCallback() {
        mNavAdapter.callback = this
        viewModel.setNavigator(this)
    }

    private fun setListeners() {
        val toggle = ActionBarDrawerToggle(
                this, viewDataBinding.drawerLayout, viewDataBinding.bar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        viewDataBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewDataBinding.navFooterSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        viewDataBinding.navFooterAddSchedule.setOnClickListener {
            navigate {
                AddDialog.newInstance().show(supportFragmentManager, "add_dialog")
            }
        }

        viewDataBinding.bar.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
                val type = getCurrentScheduleType()
                if (type == ScheduleTypes.STUDENT_CLASSES || type == ScheduleTypes.EMPLOYEE_CLASSES) {
                    OptionsFragment.newInstance(type).show(supportFragmentManager, "bottom_sheet")
                }
            }
        })
        viewDataBinding.barOptions.setOnClickListener {
            val type = getCurrentScheduleType()
            OptionsFragment.newInstance(type).show(supportFragmentManager, "bottom_sheet")
        }

        viewDataBinding.fabBack.setOnClickListener {
            FabCommunication.publish(OnFabClick)
        }
        compositeDisposable.addAll(
                FabCommunication.listen(OnFabShow::class.java).subscribe {
                    viewDataBinding.fabBack.show()
                },
                FabCommunication.listen(OnFabHide::class.java).subscribe {
                    viewDataBinding.fabBack.hide()
                })

        viewDataBinding.barAdd.setOnClickListener {
            addLesson()
        }
    }

    private fun setupBottomBar() {
        val type = getCurrentScheduleType()
        when (type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES -> {
                viewDataBinding.barOptions.visibility = View.VISIBLE
                viewDataBinding.barAddLayout.visibility = View.GONE
            }
            STUDENT_EXAMS -> {
                viewDataBinding.barOptions.visibility = View.GONE
                viewDataBinding.barAddLayout.visibility = View.VISIBLE
            }
            else -> {
                viewDataBinding.barOptions.visibility = View.GONE
                viewDataBinding.barAddLayout.visibility = View.GONE
            }
        }

        viewDataBinding.fabBack.hide()
    }

    private fun homeFragment() {
        val name = getCurrentScheduleName()
        val type = getCurrentScheduleType()

        val fragment = if (name.isNullOrEmpty() || type == -1) {
            StartFragment.newInstance()
        } else {
            val argInfo = ScheduleInformation(name!!, type)
            when (type) {
                STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(argInfo)
                STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(argInfo)
                else -> throw IllegalStateException("Invalid type")
            }
        }

        supportFragmentManager.transaction {
            replace(viewDataBinding.navHostFragment.id, fragment)
        }
    }

    private fun navigate(action: () -> Unit) {
        viewDataBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                action.invoke()
                viewDataBinding.drawerLayout.removeDrawerListener(this)
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })
        viewDataBinding.drawerLayout.closeDrawers()
    }

    private fun getCurrentScheduleName() = defaultSharedPreferences.getString(SCHEDULE_NAME, "")

    private fun getCurrentScheduleType() = defaultSharedPreferences.getInt(SCHEDULE_TYPE, -1)
}