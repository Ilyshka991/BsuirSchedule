package com.pechuro.bsuirschedule.feature.main.itemoptions

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class ItemOptionEvent : BaseEvent() {
    class OnLessonDeleted(val id: Int) : ItemOptionEvent()
}
