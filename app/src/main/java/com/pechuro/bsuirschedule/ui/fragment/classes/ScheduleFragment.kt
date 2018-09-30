package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentSheduleBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import javax.inject.Inject

class ScheduleFragment : BaseFragment<FragmentSheduleBinding, ScheduleFragmentViewModel>() {

    private lateinit var binding: FragmentSheduleBinding
    @Inject
    lateinit var mPagerAdapter: SchedulePagerAdapter

    companion object {
        fun newInstance(): ScheduleFragment {
            val args = Bundle()
            val fragment = ScheduleFragment()
            fragment.arguments = args
            return fragment
        }
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
        setup()
    }

    private fun setup() {
        binding.viewPager.adapter = mPagerAdapter

        binding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                binding.tabLayout))


        for (i in 0..31) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(i.toString()))
        }

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