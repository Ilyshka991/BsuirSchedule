package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import javax.inject.Inject

class SchedulePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getCount() = 0

    override fun getItem(position: Int): Fragment {
        return Fragment()
    }
}