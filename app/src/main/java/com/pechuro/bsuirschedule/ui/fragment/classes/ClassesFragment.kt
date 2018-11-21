package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_ALL
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_NUMBER
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_WEEK
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.FabEvent
import com.pechuro.bsuirschedule.ui.base.BaseFragment
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
    @Inject
    lateinit var sharedPref: RxSharedPreferences

    private val scheduleInfo by lazy {
        (arguments?.getParcelable(ARG_INFO) as? ScheduleInformation)
    }

    private var _viewType = VIEW_TYPE_DAY
    private var _subgroupNumber = SUBGROUP_ALL

    override val viewModel: ClassesFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ClassesFragmentViewModel::class.java)
    override val bindingVariables: Map<Int, Any>?
        get() = null
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValues()
        setupView()
        setListeners()
        inflateLayout()
    }

    private fun inflateLayout(currentItemPosition: Int = 0) {
        val info = mutableListOf<ClassesBaseInformation>()

        viewDataBinding.tabLayout.removeAllTabs()

        if (scheduleInfo == null) {
            return
        }

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

    private fun initValues() {
        _viewType = sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).get()
        _subgroupNumber = sharedPref.getInteger(SUBGROUP_NUMBER, SUBGROUP_ALL).get()
    }

    private fun setListeners() {
        compositeDisposable.addAll(
                EventBus.listen(FabEvent::class.java).subscribe {
                    when (it) {
                        is FabEvent.OnFabClick -> viewDataBinding.viewPager.setCurrentItem(0, true)
                        is FabEvent.OnFabShowPos -> if (viewDataBinding.viewPager.currentItem != 0) {
                            EventBus.publish(FabEvent.OnFabShow)
                        }
                    }
                },
                sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).asObservable().subscribe {
                    if (_viewType != it) {
                        _viewType = it
                        val position = if (_viewType == VIEW_TYPE_WEEK)
                            with(viewDataBinding.tabLayout) {
                                getTabAt(selectedTabPosition)?.tag.toString().getDayIndex()
                            } else 0

                        inflateLayout(position)
                    }
                },
                sharedPref.getInteger(SUBGROUP_NUMBER, SUBGROUP_ALL).asObservable().subscribe {
                    if (_subgroupNumber != it) {
                        _subgroupNumber = it
                        inflateLayout(viewDataBinding.viewPager.currentItem)
                    }
                })

        viewDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewDataBinding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })

        viewDataBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                EventBus.publish(if (position == 0) FabEvent.OnFabHide else FabEvent.OnFabShow)
            }
        })
    }

    private fun setupView() {
        viewDataBinding.viewPager.adapter = pagerAdapter

        viewDataBinding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                viewDataBinding.tabLayout))
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
        const val NUMBER_OF_TABS = 40
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation): ClassesFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ClassesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}