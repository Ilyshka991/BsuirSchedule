package com.pechuro.bsuirschedule.ui.activity.splash

import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import org.jetbrains.anko.doAsyncResult
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    val action = SingleLiveEvent<Action>()

    fun decideNextActivity() {
        val isInfoLoaded = doAsyncResult {
            groupRepository.isCacheNotEmpty && employeeRepository.isCacheNotEmpty
        }.get()

        if (isInfoLoaded) {
            action.call(Action.OPEN_NAVIGATION_ACTIVITY)
        } else {
            action.call(Action.OPEN_INFO_LOAD_ACTIVITY)
        }
    }
}

enum class Action {
    OPEN_NAVIGATION_ACTIVITY,
    OPEN_INFO_LOAD_ACTIVITY
}