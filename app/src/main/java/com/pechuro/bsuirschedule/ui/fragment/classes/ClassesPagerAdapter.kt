package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.ClassesBaseInformation
import javax.inject.Inject

class ClassesPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentsInfo: List<ClassesBaseInformation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        val info = fragmentsInfo[position]

        return ClassesItemFragment.newInstance(info)
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    override fun getCount() = fragmentsInfo.size
}

