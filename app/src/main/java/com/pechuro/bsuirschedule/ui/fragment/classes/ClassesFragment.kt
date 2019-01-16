package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.SUBGROUP_ALL
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.SUBGROUP_NUMBER
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE_WEEK
import com.pechuro.bsuirschedule.data.prefs.PrefsDelegate
import com.pechuro.bsuirschedule.data.prefs.PrefsEvent
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.FabEvent
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.custom.listeners.TabLayoutListener
import com.pechuro.bsuirschedule.ui.custom.listeners.ViewPagerListener
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesWeekInformation
import com.pechuro.bsuirschedule.ui.utils.EventBus
import javax.inject.Inject

class ClassesFragment : BaseFragment<FragmentViewpagerBinding, ClassesFragmentViewModel>() {

    @Inject
    lateinit var pagerAdapter: ClassesPagerAdapter

    override val viewModel: ClassesFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ClassesFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    private val scheduleInfo by lazy {
        (arguments?.getParcelable(ARG_INFO) as? ScheduleInformation)
    }

    private var _viewType: Int by PrefsDelegate(VIEW_TYPE, VIEW_TYPE_DAY)
    private var _subgroupNumber: Int by PrefsDelegate(SUBGROUP_NUMBER, SUBGROUP_ALL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setEventListeners()
        setViewListeners()
        inflateLayout()
    }

    private fun inflateLayout(currentItemPosition: Int = 0) {
        if (scheduleInfo == null) {
            return
        }

        val info = mutableListOf<ClassesBaseInformation>()
        viewDataBinding.tabLayout.removeAllTabs()

        when (_viewType) {
            VIEW_TYPE_DAY -> {
                for (i in 0 until NUMBER_OF_TABS) {
                    val (day, week, dayRu) = viewModel.getTabDate(i)
                    viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab()
                            .setText(getString(R.string.classes_tab_item, day, week)).setTag(dayRu))

                    when (scheduleInfo!!.type) {
                        ScheduleTypes.STUDENT_CLASSES -> {
                            info.add(StudentClassesDayInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu, week, _subgroupNumber))
                        }
                        ScheduleTypes.EMPLOYEE_CLASSES -> {
                            info.add(EmployeeClassesDayInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu, week))
                        }
                    }
                }
            }
            VIEW_TYPE_WEEK -> {
                for (i in 0..6) {
                    val (weekday, dayRu) = viewModel.getWeekday(i)
                    viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab()
                            .setText(weekday).setTag(dayRu))

                    when (scheduleInfo!!.type) {
                        ScheduleTypes.STUDENT_CLASSES -> {
                            info.add(StudentClassesWeekInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu, _subgroupNumber))
                        }
                        ScheduleTypes.EMPLOYEE_CLASSES -> {
                            info.add(EmployeeClassesWeekInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu))
                        }
                    }
                }
            }
        }

        viewDataBinding.viewPager.currentItem = currentItemPosition

        pagerAdapter.fragmentsInfo = info
    }

    private fun setupView() {
        viewDataBinding.viewPager.apply {
            adapter = pagerAdapter

            addOnPageChangeListener(
                    TabLayout.TabLayoutOnPageChangeListener(
                            viewDataBinding.tabLayout))
        }
    }

    private fun setEventListeners() {
        compositeDisposable.addAll(
                EventBus.listen(FabEvent::class.java).subscribe {
                    when (it) {
                        is FabEvent.OnFabClick -> viewDataBinding.viewPager.setCurrentItem(0, true)
                        is FabEvent.OnFabShowPos -> if (viewDataBinding.viewPager.currentItem != 0) {
                            EventBus.publish(FabEvent.OnFabShow)
                        }
                    }
                },

                EventBus.listen(PrefsEvent.OnChanged::class.java).subscribe {
                    when (it.key) {
                        VIEW_TYPE -> {
                            val position = if (_viewType == VIEW_TYPE_WEEK)
                                with(viewDataBinding.tabLayout) {
                                    getTabAt(selectedTabPosition)?.tag.toString().getDayIndex()
                                } else 0

                            inflateLayout(position)
                        }
                        SUBGROUP_NUMBER -> inflateLayout(viewDataBinding.viewPager.currentItem)
                    }
                })
    }

    private fun setViewListeners() {
        viewDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayoutListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewDataBinding.viewPager.currentItem = tab.position
            }
        })

        viewDataBinding.viewPager.addOnPageChangeListener(object : ViewPagerListener {
            override fun onPageSelected(position: Int) {
                EventBus.publish(if (position == 0) FabEvent.OnFabHide else FabEvent.OnFabShow)
            }
        })
    }

    private fun String.getDayIndex() = when (this) {
        "понедельник" -> 0
        "вторник" -> 1
        "среда" -> 2
        "четверг" -> 3
        "пятница" -> 4
        "суббота" -> 5
        "воскресенье" -> 6
        else -> -1
    }

    companion object {
        const val NUMBER_OF_TABS = 120
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation) = ClassesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_INFO, info)
            }
        }
    }
}