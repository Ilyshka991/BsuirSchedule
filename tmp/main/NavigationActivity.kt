package com.pechuro.bsuirschedule.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.local.preferences.PrefsConstants
import com.pechuro.bsuirschedule.local.preferences.PrefsConstants.SCHEDULE_INFO
import com.pechuro.bsuirschedule.local.preferences.PrefsConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.local.preferences.PrefsDelegate
import com.pechuro.bsuirschedule.common.SharedPreferencesEvent
import com.pechuro.bsuirschedule.feature.edit.EditLessonActivity
import com.pechuro.bsuirschedule.common.BaseActivity
import com.pechuro.bsuirschedule.widget.listeners.DrawerListener
import com.pechuro.bsuirschedule.widget.listeners.OnSwipeTouchListener
import com.pechuro.bsuirschedule.feature.main.adddialog.AddDialog
import com.pechuro.bsuirschedule.feature.main.adddialog.AddDialogEvent
import com.pechuro.bsuirschedule.feature.main.bottomoptions.BottomOptionsEvent
import com.pechuro.bsuirschedule.feature.main.bottomoptions.BottomOptionsFragment
import com.pechuro.bsuirschedule.feature.main.datepickerdialog.DatePickerDialog
import com.pechuro.bsuirschedule.feature.main.drawer.DrawerEvent
import com.pechuro.bsuirschedule.feature.main.draweroptions.DrawerOptionEvent
import com.pechuro.bsuirschedule.feature.main.draweroptions.DrawerOptionsDialog
import com.pechuro.bsuirschedule.feature.main.exam.ExamFragment
import com.pechuro.bsuirschedule.feature.main.requestupdatedialog.RequestUpdateDialog
import com.pechuro.bsuirschedule.feature.main.start.StartFragment
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.ext.transaction

class NavigationActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_navigation

    private var _lastScheduleInfo: ScheduleInformation by com.pechuro.bsuirschedule.local.preferences.PrefsDelegate(SCHEDULE_INFO, ScheduleInformation())
    private var _scheduleViewType: Int by com.pechuro.bsuirschedule.local.preferences.PrefsDelegate(com.pechuro.bsuirschedule.local.preferences.PrefsConstants.VIEW_TYPE, VIEW_TYPE_DAY)

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
        with(viewDataBinding) {
            when (type) {
                STUDENT_CLASSES, EMPLOYEE_CLASSES -> {
                    barClassesOptions.visibility = View.VISIBLE
                    barClassesDayChooser.visibility =
                            if (_scheduleViewType == VIEW_TYPE_DAY) View.VISIBLE else View.GONE
                    barExamsActionAddExam.visibility = View.GONE
                }
                STUDENT_EXAMS -> {
                    barClassesOptions.visibility = View.GONE
                    barClassesDayChooser.visibility = View.GONE
                    barExamsActionAddExam.visibility = View.VISIBLE
                }
                else -> {
                    barClassesOptions.visibility = View.GONE
                    barClassesDayChooser.visibility = View.GONE
                    barExamsActionAddExam.visibility = View.GONE
                }
            }
        }

        viewDataBinding.fabBack.hide()
    }

    private fun setViewListeners() {
        with(viewDataBinding) {
            val drawerToggle = ActionBarDrawerToggle(
                    this@NavigationActivity, drawerLayout, barLayout,
                    R.string.nav_drawer_action_open, R.string.nav_drawer_action_close)
            drawerLayout.addDrawerListener(drawerToggle)
            drawerToggle.syncState()

            val swipeHandler = object : OnSwipeTouchListener(this@NavigationActivity) {
                override fun onSwipeTop() {
                    if (_lastScheduleInfo.type == STUDENT_CLASSES || _lastScheduleInfo.type == EMPLOYEE_CLASSES) {
                        showBottomSheetDialog(_lastScheduleInfo.type)
                    }
                }
            }
            barLayout.setOnTouchListener(swipeHandler)

            barClassesOptions.setOnClickListener {
                showBottomSheetDialog(_lastScheduleInfo.type)
            }
            barClassesDayChooser.setOnClickListener {
                showDatePickerDialog()
            }
            fabBack.setOnClickListener {
                EventBus.publish(FabEvent.OnFabClick)
            }
            barExamsActionAddExam.setOnClickListener {
                addLesson()
            }
        }
    }

    private fun setCommunicationListeners() {
        compositeDisposable.addAll(
                EventBus.listen(FabEvent::class.java).subscribe {
                    when (it) {
                        is FabEvent.OnFabShow -> changeFabBackState(true)
                        is FabEvent.OnFabHide -> changeFabBackState(false)
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
                EventBus.listen(SharedPreferencesEvent.OnChanged::class.java).subscribe {
                    when (it.key) {
                        com.pechuro.bsuirschedule.local.preferences.PrefsConstants.VIEW_TYPE -> setupBottomBar()
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

    private fun changeFabBackState(show: Boolean) = with(viewDataBinding.fabBack) {
        when {
            show && !isShown -> show()
            !show && isShown -> hide()
        }
    }

    private fun onScheduleAdded() = homeFragment()

    private fun showRequestUpdateDialog(info: ScheduleInformation) =
            RequestUpdateDialog.newInstance(info).show(supportFragmentManager, RequestUpdateDialog.TAG)

    private fun showDatePickerDialog() =
            DatePickerDialog.newInstance().show(supportFragmentManager, DatePickerDialog.TAG)

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

    private inline fun navigate(crossinline action: () -> Unit) {
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