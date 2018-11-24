package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class ClassesItemEvent : BaseEvent() {
    class OnItemLongClick(val id: Int) : ClassesItemEvent()
}
