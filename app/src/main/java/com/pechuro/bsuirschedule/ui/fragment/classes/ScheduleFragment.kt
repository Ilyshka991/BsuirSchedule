package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentSheduleBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.ScheduleFragmentArgs.fromBundle
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ScheduleInformation
import javax.inject.Inject


class ScheduleFragment : BaseFragment<FragmentSheduleBinding, ScheduleFragmentViewModel>() {

    companion object {
        const val NUMBER_OF_TABS = 40
    }

    private lateinit var binding: FragmentSheduleBinding
    @Inject
    lateinit var mPagerAdapter: SchedulePagerAdapter

    private val scheduleName by lazy {
        fromBundle(arguments).scheduleName
    }
    private val scheduleType by lazy {
        fromBundle(arguments).scheduleType
    }

    override val mViewModel: ScheduleFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ScheduleFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_shedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = mViewDataBinding
        setupView()
        inflateLayout()
    }

    private fun inflateLayout() {
        val info = mutableListOf<ScheduleInformation>()

        for (i in 0 until NUMBER_OF_TABS) {
            val (day, week, dayRu) = mViewModel.getTabDate(i)
            info.add(ScheduleInformation(scheduleName, scheduleType, dayRu, week, 0))
            binding.tabLayout.addTab(binding.tabLayout.newTab()
                    .setText(getString(R.string.schedule_tab_text, day, week)))
        }

        mPagerAdapter.fragmentsInfo = info
    }

    private fun setupView() {
        binding.viewPager.adapter = mPagerAdapter

        binding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })
    }
}