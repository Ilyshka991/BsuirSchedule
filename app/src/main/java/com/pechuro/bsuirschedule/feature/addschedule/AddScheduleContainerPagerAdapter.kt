package com.pechuro.bsuirschedule.feature.addschedule

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment

class AddScheduleContainerPagerAdapter(
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    enum class FragmentType(@StringRes val titleRes: Int) {

        STUDENT(R.string.add_schedule_tab_students) {
            override fun provideFragment() = AddScheduleFragment.newInstance(this)
        },
        EMPLOYEE(R.string.add_schedule_tab_employees) {
            override fun provideFragment() = AddScheduleFragment.newInstance(this)
        };

        abstract fun provideFragment(): BaseFragment
    }

    override fun getItem(position: Int) = FragmentType.values()[position].provideFragment()

    override fun getCount() = FragmentType.values().size
}

