package com.pechuro.bsuirschedule.ui.fragment.start

import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StartFragmentViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {

    fun onClick() {
        compositeDisposable.add(repository
                .loadClasses("750502", ScheduleType.STUDENT_CLASSES)
                .subscribeOn(Schedulers.io())
                .subscribe({ }, { }))
    }
}