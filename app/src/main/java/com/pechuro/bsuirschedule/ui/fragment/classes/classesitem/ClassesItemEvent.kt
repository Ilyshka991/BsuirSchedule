package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.ui.utils.BaseEvent
import java.util.*

sealed class ClassesItemEvent : BaseEvent() {
    class OnItemLongClick(val id: Int) : ClassesItemEvent()
    class OnItemClick(val employees: ArrayList<Employee>) : ClassesItemEvent()
}
