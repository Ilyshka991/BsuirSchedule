package com.pechuro.bsuirschedule.widget.staffdetails

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bsuir.pechuro.bsuirschedule.R
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import kotlinx.android.synthetic.main.view_staff_details.view.*

class StaffDetailsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val pagerAdapter = StaffDetailsAdapter()

    init {
        inflate(context, R.layout.view_staff_details, this)
        orientation = VERTICAL
        staffDetailsViewPager.adapter = pagerAdapter
        staffDetailsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pagerAdapter.notifyDataSetChanged()
            }
        })
        TabLayoutMediator(staffDetailsTabLayout, staffDetailsViewPager) { _, _ -> }.attach()
    }

    fun setEmployees(employees: List<Employee>) {
        val staffInfo = employees.map { StaffDetailsInfo.EmployeeInfo(it) }
        updateInfo(staffInfo)
    }

    fun setGroups(groups: List<Group>) {
        val staffInfo = groups.map { StaffDetailsInfo.GroupInfo(it) }
        updateInfo(staffInfo)
    }

    private fun updateInfo(staffInfo: List<StaffDetailsInfo>) {
        pagerAdapter.submitList(staffInfo)
        staffDetailsViewPager.isVisible = staffInfo.isNotEmpty()
        staffDetailsTabLayout.isVisible = staffInfo.size > 1
    }
}