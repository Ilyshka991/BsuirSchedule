package com.pechuro.bsuirschedule.ui.fragment.employeedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.ui.fragment.employeedetails.employeeinfo.EmployeeDetailFragment
import javax.inject.Inject

class EmployeeDetailsPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragmentsInfo: List<Employee> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        val info = fragmentsInfo[position]
        return EmployeeDetailFragment.newInstance(info)
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    override fun getCount() = fragmentsInfo.size
}

