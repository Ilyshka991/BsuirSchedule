package com.pechuro.bsuirschedule.widget.staffdetails

import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group

sealed class StaffDetailsInfo {

    data class EmployeeInfo(val employee: Employee) : StaffDetailsInfo()

    data class GroupInfo(val group: Group) : StaffDetailsInfo()
}