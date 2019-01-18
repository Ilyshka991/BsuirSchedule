package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
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
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.impl.DayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.impl.WeekInformation
import com.pechuro.bsuirschedule.ui.fragment.datepickerdialog.DatePickerEvent
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

    private var viewType: Int by PrefsDelegate(VIEW_TYPE, VIEW_TYPE_DAY)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setEventListeners()
        setViewListeners()
        inflateLayout()
    }

    private fun inflateLayout(currentItemPosition: Int = NUMBER_OF_HISTORY_TABS) {
        if (scheduleInfo == null) {
            showErrorSnackbar(R.string.classes_error_inflate)
            return
        }

        val info = mutableListOf<ClassesBaseInformation>()
        viewDataBinding.tabLayout.removeAllTabs()

        when (viewType) {
            VIEW_TYPE_DAY -> {
                for (i in -NUMBER_OF_HISTORY_TABS..NUMBER_OF_TABS) {
                    val (day, week, dateTag, dayRu) = viewModel.getTabDateInfo(i)
                    viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab()
                            .setText(getString(R.string.classes_tab_item, day, week)).setTag(dayRu))
                    info.add(DayInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu, week, dateTag))
                }
            }
            VIEW_TYPE_WEEK -> {
                for (i in 0..6) {
                    val (weekday, dayRu) = viewModel.getWeekday(i)
                    viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab()
                            .setText(weekday).setTag(dayRu))
                    info.add(WeekInformation(scheduleInfo!!.name, scheduleInfo!!.type, dayRu))
                }
            }
        }

        pagerAdapter.fragmentsInfo = info
        viewDataBinding.viewPager.currentItem = currentItemPosition
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
                        is FabEvent.OnFabClick -> onBackClick()
                        is FabEvent.OnFabShowPos -> when (viewType) {
                            VIEW_TYPE_DAY -> if (viewDataBinding.viewPager.currentItem != NUMBER_OF_HISTORY_TABS) {
                                EventBus.publish(FabEvent.OnFabShow)
                            }
                            VIEW_TYPE_WEEK -> if (viewDataBinding.viewPager.currentItem != 0) {
                                EventBus.publish(FabEvent.OnFabShow)
                            }
                        }
                    }
                },
                EventBus.listen(PrefsEvent.OnChanged::class.java).subscribe {
                    onPrefsChanged(it.key)
                },
                EventBus.listen(DatePickerEvent.OnDateChoose::class.java).subscribe {
                    onDateChoosed(it.dateTag)
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
                when (viewType) {
                    VIEW_TYPE_DAY ->
                        EventBus.publish(if (position == NUMBER_OF_HISTORY_TABS) FabEvent.OnFabHide else FabEvent.OnFabShow)
                    VIEW_TYPE_WEEK ->
                        EventBus.publish(if (position == 0) FabEvent.OnFabHide else FabEvent.OnFabShow)
                }

            }
        })
    }

    private fun onPrefsChanged(key: String) {
        when (key) {
            VIEW_TYPE -> {
                val position = if (viewType == VIEW_TYPE_WEEK)
                    with(viewDataBinding.tabLayout) {
                        getTabAt(selectedTabPosition)?.tag.toString().getDayIndex()
                    } else NUMBER_OF_HISTORY_TABS

                inflateLayout(position)
            }
        }
    }

    private fun onBackClick() {
        if (viewType == VIEW_TYPE_DAY) {
            scrollTo(NUMBER_OF_HISTORY_TABS)
        } else {
            scrollTo(0)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun onDateChoosed(dateTag: String) {
        val index = with(pagerAdapter.fragmentsInfo as List<DayInformation>) {
            indexOf(find { it.dateTag == dateTag })
        }
        if (index == -1) {
            showErrorSnackbar(R.string.classes_error_incorrect_date)
        } else {
            scrollTo(index)
        }
    }

    private fun showErrorSnackbar(@StringRes messageId: Int) {
        Snackbar.make(viewDataBinding.root, getString(messageId), Snackbar.LENGTH_SHORT).show()
    }

    private fun scrollTo(position: Int) {
        viewDataBinding.viewPager.setCurrentItem(position, true)
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
        const val NUMBER_OF_TABS = 150
        const val NUMBER_OF_HISTORY_TABS = 100
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation) = ClassesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_INFO, info)
            }
        }
    }
}