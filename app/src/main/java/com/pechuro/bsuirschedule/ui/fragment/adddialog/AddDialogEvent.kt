package com.pechuro.bsuirschedule.ui.fragment.adddialog

import com.pechuro.bsuirschedule.ui.utils.BaseEvent

object OnAddDialogDismissEvent : BaseEvent()
class SetDialogCancelable(val isCancelable: Boolean) : BaseEvent()