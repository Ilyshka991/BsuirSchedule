package com.pechuro.bsuirschedule.ui.activity.splash

import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {

    fun decideNextActivity() =
            Single.fromCallable {
                groupRepository.isCacheNotEmpty().blockingGet() && employeeRepository.isCacheNotEmpty().blockingGet()
            }.subscribeOn(Schedulers.io())
                    .blockingGet()
}