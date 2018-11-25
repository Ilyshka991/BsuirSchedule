package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.f2prateek.rx.preferences2.RxSharedPreferences
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
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.custom.OnSwipeTouchListener
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogEvent
import com.pechuro.bsuirschedule.ui.fragment.bottomsheet.BottomOptionsEvent
import com.pechuro.bsuirschedule.ui.fragment.bottomsheet.BottomOptionsFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerEvent
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionEvent
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionsDialog
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragment
import com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog.RequestUpdateDialog
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragment
import com.pechuro.bsuirschedule.ui.utils.EventBus
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(),
        HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var sharedPref: RxSharedPreferences
    @Inject
    lateinit var gson: Gson

    private var _lastScheduleInfo: ScheduleInformation? = null

    override val viewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListeners()
        initValues()
        setupView()
        setViewListeners()
        setCommunicationListeners()
        if (savedInstanceState == null) homeFragment()
        setupBottomBar()
    }

    override fun onBackPressed() {
        if (viewDataBinding.drawerLayout
                        .isDrawerOpen(GravityCompat.START)) {
            viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun initValues() {
        _lastScheduleInfo = gson.fromJson(
                sharedPref.getString(SCHEDULE_INFO, "").get(),
                ScheduleInformation::class.java)
    }

    private fun setupView() {
        setSupportActionBar(viewDataBinding.barLayout)
    }

    private fun setupBottomBar() {
        val type = _lastScheduleInfo?.type
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

    private fun setListeners() {
        compositeDisposable.add(sharedPref.getString(SCHEDULE_INFO, "")
                .asObservable()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    _lastScheduleInfo = if (it.isNullOrEmpty()) null else gson.fromJson(it, ScheduleInformation::class.java)
                })
    }

    private fun setViewListeners() {
        val toggle = ActionBarDrawerToggle(
                this, viewDataBinding.drawerLayout, viewDataBinding.barLayout,
                R.string.nav_drawer_action_open, R.string.nav_drawer_action_close)
        viewDataBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewDataBinding.barLayout.setOnTouchListener(object : OnSwipeTouchListener(this@NavigationActivity) {
            override fun onSwipeTop() {
                if (_lastScheduleInfo?.type == STUDENT_CLASSES || _lastScheduleInfo?.type == EMPLOYEE_CLASSES) {
                    showBottomSheetDialog(_lastScheduleInfo!!.type)
                }
            }
        })
        viewDataBinding.barOptions.setOnClickListener {
            showBottomSheetDialog(_lastScheduleInfo!!.type)
        }

        viewDataBinding.fabBack.setOnClickListener {
            EventBus.publish(FabEvent.OnFabClick)
        }
        viewDataBinding.buttonAdd.setOnClickListener {
            addLesson()
        }
    }

    private fun setCommunicationListeners() {
        compositeDisposable.addAll(
                EventBus.listen(FabEvent::class.java).subscribe {
                    when (it) {
                        is FabEvent.OnFabShow -> viewDataBinding.fabBack.show()
                        is FabEvent.OnFabHide -> viewDataBinding.fabBack.hide()
                    }
                },

                EventBus.listen(DrawerEvent::class.java).subscribe {
                    when (it) {
                        is DrawerEvent.OnNavigate -> onNavigate(it.info)
                        is DrawerEvent.OnItemLongClick -> showDrawerOptionsDialog(it.info)
                        is DrawerEvent.OnOpenAddDialog -> showAddDialog()
                    }
                },

                EventBus.listen(DrawerOptionEvent::class.java).subscribe {
                    when (it) {
                        is DrawerOptionEvent.OnScheduleUpdated -> onScheduleUpdated(it.info)
                        is DrawerOptionEvent.OnScheduleDeleted -> onScheduleDeleted(it.info)
                    }
                },

                EventBus.listen(BottomOptionsEvent.OnAddLesson::class.java).subscribe {
                    addLesson()
                },

                EventBus.listen(AddDialogEvent.OnScheduleAdded::class.java).subscribe {
                    onScheduleAdded()
                },

                EventBus.listen(ScheduleUpdateEvent.OnRequestUpdate::class.java).subscribe {
                    showRequestUpdateDialog(it.info)
                }
        )
    }

    private fun homeFragment() {
        val info = _lastScheduleInfo

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

    private fun onScheduleUpdated(info: ScheduleInformation) {
        if (info.id == _lastScheduleInfo?.id) {
            homeFragment()
        }
    }

    private fun onScheduleDeleted(info: ScheduleInformation) {
        if (info.name == _lastScheduleInfo?.name && info.type == _lastScheduleInfo?.type) {
            changeLastScheduleInfo(null)
            homeFragment()
        }
    }

    private fun onScheduleAdded() = homeFragment()

    private fun showRequestUpdateDialog(info: ScheduleInformation) =
            RequestUpdateDialog.newInstance(info).show(supportFragmentManager, RequestUpdateDialog.TAG)

    private fun showAddDialog() =
            navigate {
                AddDialog.newInstance().show(supportFragmentManager, AddDialog.TAG)
            }

    private fun showDrawerOptionsDialog(info: ScheduleInformation) =
            DrawerOptionsDialog.newInstance(info).show(supportFragmentManager, "options_dialog")

    private fun showBottomSheetDialog(scheduleType: Int) =
            BottomOptionsFragment.newInstance(scheduleType).show(supportFragmentManager, "bottom_sheet")

    private fun onNavigate(info: ScheduleInformation) {
        val fragment = when (info.type) {
            ScheduleTypes.STUDENT_CLASSES, ScheduleTypes.EMPLOYEE_CLASSES -> ClassesFragment.newInstance(info)
            ScheduleTypes.STUDENT_EXAMS, ScheduleTypes.EMPLOYEE_EXAMS -> ExamFragment.newInstance(info)
            else -> throw IllegalArgumentException("Invalid type")
        }
        navigate {
            supportFragmentManager.transaction {
                replace(viewDataBinding.navHostFragment.id, fragment)
                changeLastScheduleInfo(info)
                setupBottomBar()
            }
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

    private fun addLesson() {
        val intent = EditLessonActivity.newIntent(this, _lastScheduleInfo!!)
        startActivity(intent)
    }

    private fun changeLastScheduleInfo(info: ScheduleInformation?) {
        defaultSharedPreferences.edit {
            putString(SCHEDULE_INFO, gson.toJson(info))
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }
}