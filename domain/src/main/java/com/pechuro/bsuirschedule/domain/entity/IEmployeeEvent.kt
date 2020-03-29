package com.pechuro.bsuirschedule.domain.entity

interface IEmployeeEvent : IScheduleItem {
    val studentGroups: List<Group>
}
