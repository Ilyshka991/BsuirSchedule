package com.pechuro.bsuirschedule.ui.fragment.adddialog

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragment
import javax.inject.Inject

class AddDialogPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = when (position) {
        0 -> AddFragment.newInstance(STUDENT)
        1 -> AddFragment.newInstance(EMPLOYEE)
        else -> throw IllegalStateException("Wrong position")
    }

    override fun getCount() = 2
}

