package com.pechuro.bsuirschedule.data.prefs

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class PrefsEvent : BaseEvent() {
    class OnChanged(val key: String) : PrefsEvent()
}