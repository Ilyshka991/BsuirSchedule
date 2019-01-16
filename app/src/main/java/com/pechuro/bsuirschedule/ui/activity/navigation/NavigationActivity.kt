package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.SCHEDULE_INFO
import com.pechuro.bsuirschedule.data.prefs.PrefsDelegate
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.custom.listeners.DrawerListener
import com.pechuro.bsuirschedule.ui.custom.listeners.OnSwipeTouchListener
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogEvent
import com.pechuro.bsuirschedule.ui.fragment.bottomoptions.BottomOptionsEvent
import com.pechuro.bsuirschedule.ui.fragment.bottomoptions.BottomOptionsFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerEvent
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionEvent
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionsDialog
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragment
import com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog.RequestUpdateDialog
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragment
import com.pechuro.bsuirschedule.ui.utils.EventBus
import com.pechuro.bsuirschedule.ui.utils.transaction

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>() {

    override val viewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation

    private var _lastScheduleInfo: ScheduleInformation by PrefsDelegate(SCHEDULE_INFO, ScheduleInformation())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun setupView() {
        setSupportActionBar(viewDataBinding.barLayout)
    }

    private fun setupBottomBar() {
        val type = _lastScheduleInfo.type
        when (type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES -> {
                viewDataBinding.barClassesOptions.visibility = View.VISIBLE
                viewDataBinding.barExamsActionAddExam.visibility = View.GONE
            }
            STUDENT_EXAMS -> {
                viewDataBinding.barClassesOptions.visibility = View.GONE
                viewDataBinding.barExamsActionAddExam.visibility = View.VISIBLE
            }
            else -> {
                viewDataBinding.barClassesOptions.visibility = View.GONE
                viewDataBinding.barExamsActionAddExam.visibility = View.GONE
            }
        }

        viewDataBinding.fabBack.hide()
    }

    private fun setViewListeners() {
        val drawerToggle = ActionBarDrawerToggle(
                this, viewDataBinding.drawerLayout, viewDataBinding.barLayout,
                R.string.nav_drawer_action_open, R.string.nav_drawer_action_close)
        viewDataBinding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val swipeHandler = object : OnSwipeTouchListener(this@NavigationActivity) {
            override fun onSwipeTop() {
                if (_lastScheduleInfo.type == STUDENT_CLASSES || _lastScheduleInfo.type == EMPLOYEE_CLASSES) {
                    showBottomSheetDialog(_lastScheduleInfo.type)
                }
            }
        }
        viewDataBinding.barLayout.setOnTouchListener(swipeHandler)

        viewDataBinding.barClassesOptions.setOnClickListener {
            showBottomSheetDialog(_lastScheduleInfo.type)
        }
        viewDataBinding.fabBack.setOnClickListener {
            EventBus.publish(FabEvent.OnFabClick)
        }
        viewDataBinding.barExamsActionAddExam.setOnClickListener {
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

        val fragment = if (info.isEmpty()) {
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
        if (info.id == _lastScheduleInfo.id) {
            homeFragment()
        }
    }

    private fun onScheduleDeleted(info: ScheduleInformation) {
        if (info.name == _lastScheduleInfo.name && info.type == _lastScheduleInfo.type) {
            _lastScheduleInfo = ScheduleInformation()
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
            DrawerOptionsDialog.newInstance(info).show(supportFragmentManager, DrawerOptionsDialog.TAG)

    private fun showBottomSheetDialog(scheduleType: Int) =
            BottomOptionsFragment.newInstance(scheduleType).show(supportFragmentManager, BottomOptionsFragment.TAG)

    private fun onNavigate(info: ScheduleInformation) {
        val fragment = when (info.type) {
            ScheduleTypes.STUDENT_CLASSES, ScheduleTypes.EMPLOYEE_CLASSES -> ClassesFragment.newInstance(info)
            ScheduleTypes.STUDENT_EXAMS, ScheduleTypes.EMPLOYEE_EXAMS -> ExamFragment.newInstance(info)
            else -> throw IllegalArgumentException("Invalid type")
        }
        navigate {
            supportFragmentManager.transaction {
                replace(viewDataBinding.navHostFragment.id, fragment)
                _lastScheduleInfo = info
                setupBottomBar()
            }
        }
    }

    private fun navigate(action: () -> Unit) {
        viewDataBinding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerClosed(drawerView: View) {
                action()
                viewDataBinding.drawerLayout.removeDrawerListener(this)
            }
        })
        viewDataBinding.drawerLayout.closeDrawers()
    }

    private fun addLesson() {
        val intent = EditLessonActivity.newIntent(this, _lastScheduleInfo)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }
}