package com.pechuro.bsuirschedule.feature.main.addschedule

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.feature.main.addschedule.fragment.AddScheduleFragment

class AddScheduleContainerDialogPagerAdapter(
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    enum class FragmentType(@StringRes val titleRes: Int) {

        STUDENT(R.string.add_dialog_tab_item_students) {
            override fun provideFragment() = AddScheduleFragment.newInstance(this)
        },
        EMPLOYEE(R.string.add_dialog_tab_item_employees) {
            override fun provideFragment() = AddScheduleFragment.newInstance(this)
        };

        abstract fun provideFragment(): BaseFragment
    }

    override fun getItem(position: Int) = FragmentType.values()[position].provideFragment()

    override fun getCount() = FragmentType.values().size
}

