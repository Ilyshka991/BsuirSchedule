package com.pechuro.bsuirschedule.feature.loadinfo

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class LoadInfoEvent : BaseEvent() {

    object OnComplete : LoadInfoEvent()
}