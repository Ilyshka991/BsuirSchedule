package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation

sealed class AddFragmentEvent

class OnError(val messageId: Int) : AddFragmentEvent()
object OnClearError : AddFragmentEvent()
class OnSuccess(val info: ScheduleInformation) : AddFragmentEvent()
object OnLoading : AddFragmentEvent()
object OnCancel : AddFragmentEvent()