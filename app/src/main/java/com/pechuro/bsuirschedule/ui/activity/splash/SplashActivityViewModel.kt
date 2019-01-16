package com.pechuro.bsuirschedule.ui.activity.splash

import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    val action = SingleLiveEvent<Action>()

    fun decideNextActivity() {
        Single.fromCallable {
            groupRepository.isCacheNotEmpty().blockingGet() && employeeRepository.isCacheNotEmpty().blockingGet()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        action.call(Action.OPEN_NAVIGATION_ACTIVITY)
                    } else {
                        action.call(Action.OPEN_INFO_LOAD_ACTIVITY)
                    }
                }, {})
                .let(compositeDisposable::add)
    }
}

enum class Action {
    OPEN_NAVIGATION_ACTIVITY,
    OPEN_INFO_LOAD_ACTIVITY
}