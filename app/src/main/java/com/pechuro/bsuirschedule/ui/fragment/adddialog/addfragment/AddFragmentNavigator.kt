package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import com.pechuro.bsuirschedule.ui.base.BaseNavigator

interface AddFragmentNavigator : BaseNavigator {
    fun onError(messageId: Int)

    fun onClearError()

    fun onSuccess(scheduleName: String, scheduleType: Int)

    fun onLoading()

    fun onCancel()
}