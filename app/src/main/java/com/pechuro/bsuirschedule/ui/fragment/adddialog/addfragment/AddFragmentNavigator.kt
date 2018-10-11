package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

interface AddFragmentNavigator {
    fun onError(messageId: Int)

    fun onClearError()

    fun onSuccess()

    fun onLoading()
}