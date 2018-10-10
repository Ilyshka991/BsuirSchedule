package com.pechuro.bsuirschedule.ui.activity.splash

import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    lateinit var navigator: SplashNavigator

    fun decideNextActivity() =
            doAsync {
                if (groupRepository.isCacheNotEmpty && employeeRepository.isCacheNotEmpty) {
                    navigator.openNavigationActivity()
                } else {
                    navigator.openInfoLoadActivity()
                }
            }
}