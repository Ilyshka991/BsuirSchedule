package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.ClassesDayFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.ClassesWeekFragment
import javax.inject.Inject

class ClassesPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentsInfo: List<ClassesBaseInformation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        val info = fragmentsInfo[position]
        return when (info) {
            is ClassesDayInformation -> ClassesDayFragment.newInstance(info)
            is ClassesWeekInformation -> ClassesWeekFragment.newInstance(info)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getCount() = fragmentsInfo.size
}

