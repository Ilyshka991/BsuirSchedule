package com.pechuro.bsuirschedule.feature.main.bottomoptions

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class BottomOptionsEvent : BaseEvent() {
    object OnAddLesson : BottomOptionsEvent()
}
