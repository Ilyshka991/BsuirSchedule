package com.pechuro.bsuirschedule.feature.stafflist

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group

sealed class StaffItemInformation(val type: Int) {

    companion object {
        const val TYPE_GROUP = 1
        const val TYPE_EMPLOYEE = 2
        const val TYPE_EMPTY = 3
        const val TYPE_AUDITORY = 4
    }

    data class GroupInfo(val group: Group) : StaffItemInformation(TYPE_GROUP)

    data class EmployeeInfo(val employee: Employee) : StaffItemInformation(TYPE_EMPLOYEE)

    data class AuditoryInfo(val auditory: Auditory) : StaffItemInformation(TYPE_AUDITORY)

    object Empty : StaffItemInformation(TYPE_EMPTY)
}