package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragment
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ScheduleInformation
import javax.inject.Inject

class SchedulePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentsInfo: List<ScheduleInformation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return ListFragment.newInstance(fragmentsInfo[position])
    }

    override fun getCount() = fragmentsInfo.size
}

