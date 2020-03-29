package com.pechuro.bsuirschedule.domain.entity

interface IGroupEvent : IScheduleItem {
    val employees: List<Employee>
}
