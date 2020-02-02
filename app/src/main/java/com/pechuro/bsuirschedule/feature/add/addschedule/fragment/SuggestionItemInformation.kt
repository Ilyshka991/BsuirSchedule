package com.pechuro.bsuirschedule.feature.add.addschedule.fragment

import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group

sealed class SuggestionItemInformation(val type: Int) {

    companion object {
        const val TYPE_GROUP = 1
        const val TYPE_EMPLOYEE = 2
    }

    data class GroupInfo(val group: Group) : SuggestionItemInformation(TYPE_GROUP)

    data class EmployeeInfo(val employee: Employee) : SuggestionItemInformation(TYPE_EMPLOYEE)
}