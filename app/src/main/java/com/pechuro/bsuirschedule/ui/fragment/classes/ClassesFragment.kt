package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_ALL
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_NUMBER
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_WEEK
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.*
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.EmployeeClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl.StudentClassesWeekInformation
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ClassesFragment : BaseFragment<FragmentViewpagerBinding, ClassesFragmentViewModel>() {
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

    @Inject
    lateinit var mPagerAdapter: ClassesPagerAdapter
    @Inject
    lateinit var sharedPref: RxSharedPreferences

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val scheduleName by lazy {
        (arguments?.getParcelable(ARG_INFO) as? ScheduleInformation)?.name ?: ""
    }
    private val scheduleType by lazy {
        (arguments?.getParcelable(ARG_INFO) as? ScheduleInformation)?.type ?: -1
    }

    private var viewType = VIEW_TYPE_DAY
    private var subgroupNumber = SUBGROUP_ALL

    override val mViewModel: ClassesFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ClassesFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValues()
        setupView()
        setListeners()
        inflateLayout()
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }

    private fun inflateLayout(currentItemPosition: Int = 0) {
        val info = mutableListOf<ClassesBaseInformation>()

        mViewDataBinding.tabLayout.removeAllTabs()

        when (viewType) {
            VIEW_TYPE_DAY -> {
                for (i in 0 until NUMBER_OF_TABS) {
                    val (day, week, dayRu) = mViewModel.getTabDate(i)
                    mViewDataBinding.tabLayout.addTab(mViewDataBinding.tabLayout.newTab()
                            .setText(getString(R.string.schedule_tab_text, day, week)).setTag(dayRu))

                    when (scheduleType) {
                        ScheduleTypes.STUDENT_CLASSES -> {
                            info.add(StudentClassesDayInformation(scheduleName, scheduleType, dayRu, week, subgroupNumber))
                        }
                        ScheduleTypes.EMPLOYEE_CLASSES -> {
                            info.add(EmployeeClassesDayInformation(scheduleName, scheduleType, dayRu, week))
                        }
                    }
                }
            }
            VIEW_TYPE_WEEK -> {
                for (i in 0..6) {
                    val (weekday, dayRu) = mViewModel.getWeekday(i)
                    mViewDataBinding.tabLayout.addTab(mViewDataBinding.tabLayout.newTab()
                            .setText(weekday).setTag(dayRu))

                    when (scheduleType) {
                        ScheduleTypes.STUDENT_CLASSES -> {
                            info.add(StudentClassesWeekInformation(scheduleName, scheduleType, dayRu, subgroupNumber))
                        }
                        ScheduleTypes.EMPLOYEE_CLASSES -> {
                            info.add(EmployeeClassesWeekInformation(scheduleName, scheduleType, dayRu))
                        }
                    }
                }
            }
        }

        mViewDataBinding.viewPager.currentItem = currentItemPosition

        mPagerAdapter.fragmentsInfo = info
    }

    private fun initValues() {
        viewType = sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).get()
        subgroupNumber = sharedPref.getInteger(SUBGROUP_NUMBER, SUBGROUP_ALL).get()
    }

    private fun setListeners() {
        compositeDisposable.addAll(
                FabCommunication.listen(OnFabClick::class.java)
                        .subscribe {
                            mViewDataBinding.viewPager.setCurrentItem(0, true)
                        },
                FabCommunication.listen(OnFabShowPos::class.java).subscribe {
                    if (mViewDataBinding.viewPager.currentItem != 0) {
                        FabCommunication.publish(OnFabShow)
                    }
                },

                sharedPref.getInteger(VIEW_TYPE).asObservable().subscribe {
                    if (viewType != it) {
                        viewType = it

                        val position = if (viewType == VIEW_TYPE_WEEK)
                            with(mViewDataBinding.tabLayout) {
                                getTabAt(selectedTabPosition)?.tag.toString().getDayIndex()
                            } else 0

                        inflateLayout(position)
                    }
                },
                sharedPref.getInteger(SUBGROUP_NUMBER).asObservable().subscribe {
                    if (subgroupNumber != it) {
                        subgroupNumber = it
                        inflateLayout(mViewDataBinding.viewPager.currentItem)
                    }
                })

        mViewDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewDataBinding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })

        mViewDataBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                FabCommunication.publish(if (position == 0) OnFabHide else OnFabShow)
            }
        })
    }

    private fun setupView() {
        mViewDataBinding.viewPager.adapter = mPagerAdapter

        mViewDataBinding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                mViewDataBinding.tabLayout))
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
}