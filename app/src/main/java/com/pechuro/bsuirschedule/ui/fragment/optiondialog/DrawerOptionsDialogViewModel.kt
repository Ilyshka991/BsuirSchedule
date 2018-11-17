package com.pechuro.bsuirschedule.ui.fragment.optiondialog

import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerOptionsDialogViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val command = SingleLiveEvent<DrawerOptionsEvent>()

    fun update(info: ScheduleInformation) {
        compositeDisposable.add(
                repository.update(info)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            command.call(OnUpdate(info))
                        }, {})
        )
    }

    fun delete(info: ScheduleInformation) {
        compositeDisposable.add(
                repository.delete(info.name, info.type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            command.call(OnDelete(info))
                        }, {})
        )
    }

}