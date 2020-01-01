package com.pechuro.bsuirschedule.common

sealed class SharedPreferencesEvent : BaseEvent() {

    class OnChanged(val key: String) : SharedPreferencesEvent()
}