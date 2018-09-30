package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragment
import javax.inject.Inject

class SchedulePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getCount() = 31

    override fun getItem(position: Int): Fragment {
        return ListFragment.newInstance()
    }
}