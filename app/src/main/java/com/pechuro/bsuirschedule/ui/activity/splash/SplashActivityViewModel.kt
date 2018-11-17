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
    val command = SingleLiveEvent<SplashEvent>()

    fun decideNextActivity() {
        val isInfoLoaded = doAsyncResult {
            groupRepository.isCacheNotEmpty && employeeRepository.isCacheNotEmpty
        }.get()

        if (isInfoLoaded) {
            command.call(OpenNavigationActivity)
        } else {
            command.call(OpenInfoLoadActivity)
        }
    }
}