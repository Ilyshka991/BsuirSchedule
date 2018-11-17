package com.pechuro.bsuirschedule.ui.activity.splash

import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel<SplashNavigator>() {

    fun decideNextActivity() =
            doAsync {
                if (groupRepository.isCacheNotEmpty && employeeRepository.isCacheNotEmpty) {
                    getNavigator()?.openNavigationActivity()
                } else {
                    getNavigator()?.openInfoLoadActivity()
                }
            }
}