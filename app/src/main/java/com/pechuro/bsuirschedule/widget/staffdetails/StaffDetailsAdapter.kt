package com.pechuro.bsuirschedule.widget.staffdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.widget.staffdetails.StaffDetailsInfo.EmployeeInfo
import com.pechuro.bsuirschedule.widget.staffdetails.StaffDetailsInfo.GroupInfo
import kotlinx.android.synthetic.main.item_employee_details.*
import kotlinx.android.synthetic.main.item_group_details.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StaffDetailsInfo>() {

    override fun areItemsTheSame(
            oldItem: StaffDetailsInfo,
            newItem: StaffDetailsInfo
    ) = oldItem == newItem

    override fun areContentsTheSame(
            oldItem: StaffDetailsInfo,
            newItem: StaffDetailsInfo
    ) = oldItem == newItem
}

private const val EMPLOYEE_TYPE_LAYOUT_RES = R.layout.item_employee_details
private const val GROUP_TYPE_LAYOUT_RES = R.layout.item_group_details

class StaffDetailsAdapter : ListAdapter<StaffDetailsInfo, BaseViewHolder<StaffDetailsInfo>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeLayoutRes: Int): BaseViewHolder<StaffDetailsInfo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewTypeLayoutRes, parent, false)
        return when (viewTypeLayoutRes) {
            EMPLOYEE_TYPE_LAYOUT_RES -> EmployeeViewHolder(view)
            GROUP_TYPE_LAYOUT_RES -> GroupViewHolder(view)
            else -> throw IllegalArgumentException("Not supported type: $viewTypeLayoutRes")
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is EmployeeInfo -> EMPLOYEE_TYPE_LAYOUT_RES
        is GroupInfo -> GROUP_TYPE_LAYOUT_RES
    }

    override fun onBindViewHolder(
            holder: BaseViewHolder<StaffDetailsInfo>,
            position: Int
    ) = holder.onBind(getItem(position))

    private class EmployeeViewHolder(view: View) : BaseViewHolder<EmployeeInfo>(view) {

        override fun onBind(data: EmployeeInfo) {
            val employee = data.employee
            employeeDetailsFullName.text = employee.getFullName()
            employeeDetailsAdditionalInfo.text = employee.getAdditionalInfoText()
            employeeDetailsPhoto.loadAvatar(employee.photoLink)
        }

        private fun Employee.getFullName() = "$firstName $middleName $lastName"

        private fun Employee.getAdditionalInfoText() =
                "$rank${if (rank.isNotEmpty()) ", " else ""}${department.name}"

        private fun ImageView.loadAvatar(url: String) {
            Glide.with(itemView)
                    .load(url)
                    .override(SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.employee_placeholder)
                    .error(R.drawable.employee_placeholder)
                    .circleCrop()
                    .into(this)
        }
    }

    private class GroupViewHolder(view: View) : BaseViewHolder<GroupInfo>(view) {

        override fun onBind(data: GroupInfo) {
            val group = data.group
            groupDetailsNumber.text = group.number
        }
    }
}