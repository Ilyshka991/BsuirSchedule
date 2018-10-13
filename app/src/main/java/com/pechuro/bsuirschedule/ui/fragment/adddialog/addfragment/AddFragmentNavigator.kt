package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

interface AddFragmentNavigator {
    fun onError(messageId: Int)

    fun onClearError()

    fun onSuccess(scheduleName: String, scheduleType: Int)

    fun onLoading()

    fun onCancel()
}