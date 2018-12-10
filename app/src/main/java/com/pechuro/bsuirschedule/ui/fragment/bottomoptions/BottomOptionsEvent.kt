package com.pechuro.bsuirschedule.ui.fragment.bottomoptions

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class BottomOptionsEvent : BaseEvent() {
    object OnAddLesson : BottomOptionsEvent()
}
