package com.pechuro.bsuirschedule.feature.main.classes.classesitem

import com.pechuro.bsuirschedule.toDelete.entity.Employee
import com.pechuro.bsuirschedule.common.BaseEvent
import java.util.*

sealed class ClassesItemEvent : BaseEvent() {
    class OnItemLongClick(val id: Int) : ClassesItemEvent()
    class OnItemClick(val employees: ArrayList<Employee>) : ClassesItemEvent()
}
