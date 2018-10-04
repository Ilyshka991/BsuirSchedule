package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragment
import javax.inject.Inject

class SchedulePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragments: List<DayScheduleInformation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return ListFragment.newInstance(fragments[position])
    }

    override fun getCount() = fragments.size
}