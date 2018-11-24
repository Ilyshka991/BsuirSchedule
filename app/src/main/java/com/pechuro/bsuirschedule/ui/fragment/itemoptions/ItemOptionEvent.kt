package com.pechuro.bsuirschedule.ui.fragment.itemoptions

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class ItemOptionEvent : BaseEvent() {
    class OnLessonDeleted(val id: Int) : ItemOptionEvent()
}
