package com.pechuro.bsuirschedule.ui.fragment.adddialog

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class AddDialogEvent : BaseEvent() {
    object OnDismiss : AddDialogEvent()
    class SetCancelable(val isCancelable: Boolean) : AddDialogEvent()
}
