package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragment
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ClassesDayInformation
import javax.inject.Inject

class ClassesPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentsInfo: List<ClassesDayInformation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return ListFragment.newInstance(fragmentsInfo[position])
    }

    override fun getCount() = fragmentsInfo.size
}

