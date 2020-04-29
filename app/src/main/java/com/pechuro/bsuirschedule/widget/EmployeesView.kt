package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Employee
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_employee_details.*
import kotlinx.android.synthetic.main.view_employees.view.*

class EmployeesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val adapter = EmployeeAdapter()

    var employees: List<Employee> = emptyList()
        set(value) {
            field = value

            viewPager.adapter?.notifyDataSetChanged()

            viewPager.isVisible = employees.isNotEmpty()
            tabLayout.isVisible = employees.size > 1
        }

    init {
        View.inflate(context, R.layout.view_employees, this)
        orientation = VERTICAL

        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                adapter.notifyDataSetChanged()
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            tab.view.isClickable = false
        }.attach()
    }

    private val viewPager: ViewPager2 get() = employeesViewInfoViewPager

    private val tabLayout: TabLayout get() = employeesViewInfoTabLayout

    private inner class EmployeeAdapter : RecyclerView.Adapter<EmployeeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee_details, parent, false)
            return EmployeeViewHolder(view)
        }

        override fun getItemCount(): Int = employees.size

        override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
            val employee = employees[position]
            holder.itemView.tag = employee
            holder.employeeDetailsFullName.text = with(employee) {
                "$firstName $middleName $lastName"
            }
            holder.employeeDetailsAdditionalInfo.text = employee.getAdditionalInfoText()
            Glide.with(holder.itemView)
                    .load(employee.photoLink)
                    .override(Target.SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.employee_placeholder)
                    .error(R.drawable.employee_placeholder)
                    .circleCrop()
                    .into(holder.employeeDetailsPhoto)
        }

        private fun Employee.getAdditionalInfoText(): String =
                "$rank${if (rank.isNotEmpty()) ", " else ""}${department.name}"
    }

    private class EmployeeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}