package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

sealed class AddFragmentEvent

class OnError(val messageId: Int) : AddFragmentEvent()
object OnClearError : AddFragmentEvent()
class OnSuccess(val scheduleName: String, val scheduleType: Int) : AddFragmentEvent()
object OnLoading : AddFragmentEvent()
object OnCancel : AddFragmentEvent()