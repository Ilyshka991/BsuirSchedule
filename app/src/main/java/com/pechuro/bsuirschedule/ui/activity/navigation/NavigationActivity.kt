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
import com.google.gson.Gson
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SCHEDULE_INFO
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
import com.pechuro.bsuirschedule.ui.fragment.optiondialog.DrawerOptionsDialog
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
        OptionsFragment.ScheduleOptionsCallback, DrawerOptionsDialog.DrawerOptionsCallback {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var navAdapter: NavItemAdapter

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
        setCommunicationListeners()
        if (savedInstanceState == null) {
            homeFragment()
        }
        setupBottomBar()
        subscribeToLiveData()
        setEventListeners()
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

    override fun onItemUpdated(info: ScheduleInformation) {
        val currentScheduleInfo = getCurrentScheduleInfo()
        if (info.id == currentScheduleInfo?.id) {
            homeFragment()
        }
    }

    override fun onItemDeleted(info: ScheduleInformation) {
        val currentScheduleInfo = getCurrentScheduleInfo()
        if (info.name == currentScheduleInfo?.name && info.type == currentScheduleInfo.type) {
            changeLastScheduleInfo(null)
            homeFragment()
        }
    }

    override fun onNavigate(info: ScheduleInformation) {
        val fragment = when (info.type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(info)
            STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(info)
            else -> throw IllegalStateException("Invalid type")
        }
        navigate {
            supportFragmentManager.transaction {
                replace(viewDataBinding.navHostFragment.id, fragment)

                changeLastScheduleInfo(info)

                setupBottomBar()
            }
        }
    }

    override fun onDrawerItemLongClick(info: ScheduleInformation) {
        DrawerOptionsDialog.newInstance(info).show(supportFragmentManager, "options_dialog")
    }

    override fun onAddDialogDismiss() {
        homeFragment()
        setupBottomBar()
    }

    override fun addLesson() {
        val info = getCurrentScheduleInfo()!!

        val intent = EditLessonActivity.newIntent(this, info)
        startActivity(intent)
    }

    private fun setCommunicationListeners() {
        compositeDisposable.addAll(
                FabCommunication.listen(OnFabShow::class.java).subscribe {
                    viewDataBinding.fabBack.show()
                },
                FabCommunication.listen(OnFabHide::class.java).subscribe {
                    viewDataBinding.fabBack.hide()
                })
    }

    private fun changeLastScheduleInfo(info: ScheduleInformation?) {
        defaultSharedPreferences.edit {
            putString(SCHEDULE_INFO, gson.toJson(info))
        }
    }

    private fun setEventListeners() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is OnRequestUpdate -> {
                    Toast.makeText(this, "${it.info.name} - ${it.info.type} need update", Toast.LENGTH_LONG).show()

                    viewModel.updateSchedule(it.info)
                }

                is OnScheduleUpdated -> {
                    Snackbar.make(viewDataBinding.contentLayout, "${it.info.name} - ${it.info.type} updated!!!!!!!!!!", Snackbar.LENGTH_SHORT).show()

                    val currentScheduleInfo = getCurrentScheduleInfo()
                    if (it.info.name == currentScheduleInfo?.name && it.info.type == currentScheduleInfo.type) {
                        homeFragment()
                    }
                }

                is OnScheduleUpdateFail -> {
                    Snackbar.make(viewDataBinding.contentLayout, "${it.info.name} - ${it.info.type} update fail !!!!!!!!!!", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun subscribeToLiveData() {
        viewModel.menuItems.observe(this, Observer {
            navAdapter.setItems(it)
        })
    }

    private fun setupView() {
        setSupportActionBar(bar)

        layoutManager.orientation = RecyclerView.VERTICAL
        viewDataBinding.navItemList.layoutManager = layoutManager
        viewDataBinding.navItemList.adapter = navAdapter
    }

    private fun setCallback() {
        navAdapter.callback = this
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
                val info = getCurrentScheduleInfo()
                if (info?.type == ScheduleTypes.STUDENT_CLASSES || info?.type == ScheduleTypes.EMPLOYEE_CLASSES) {
                    OptionsFragment.newInstance(info.type).show(supportFragmentManager, "bottom_sheet")
                }
            }
        })
        viewDataBinding.barOptions.setOnClickListener {
            val type = getCurrentScheduleInfo()?.type!!
            OptionsFragment.newInstance(type).show(supportFragmentManager, "bottom_sheet")
        }

        viewDataBinding.fabBack.setOnClickListener {
            FabCommunication.publish(OnFabClick)
        }

        viewDataBinding.barAdd.setOnClickListener {
            addLesson()
        }
    }

    private fun setupBottomBar() {
        val type = getCurrentScheduleInfo()?.type
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
        val info = getCurrentScheduleInfo()

        val fragment = if (info == null) {
            StartFragment.newInstance()
        } else {
            when (info.type) {
                STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(info)
                STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(info)
                else -> throw IllegalStateException("Invalid type")
            }
        }

        supportFragmentManager.transaction {
            replace(viewDataBinding.navHostFragment.id, fragment)
        }
        setupBottomBar()
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

    private fun getCurrentScheduleInfo(): ScheduleInformation? {
        val json = defaultSharedPreferences.getString(SCHEDULE_INFO, "")
        return if (json.isNullOrEmpty()) null else gson.fromJson(json, ScheduleInformation::class.java)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }
}