package com.pechuro.bsuirschedule.feature.main.adddialog

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class AddDialogEvent : BaseEvent() {
    object OnSuccess : AddDialogEvent()
    object OnScheduleAdded : AddDialogEvent()
    class OnLoading(val isEnabled: Boolean) : AddDialogEvent()
}
