package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import androidx.databinding.ObservableBoolean
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerOptionsDialogViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val status = SingleLiveEvent<Status>()
    val isLoading = ObservableBoolean()
    val isError = ObservableBoolean()

    fun update(info: ScheduleInformation) {
        isLoading.set(true)
        isError.set(false)
        compositeDisposable.add(
                repository.update(info)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            status.call(Status.OnUpdated(info))
                        }, {
                            isError.set(true)
                            isLoading.set(false)
                        })
        )
    }

    fun delete(info: ScheduleInformation) {
        compositeDisposable.add(
                repository.delete(info.name, info.type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            status.call(Status.OnDeleted(info))
                        }, {})
        )
    }

    fun cancel() = status.call(Status.OnCancel)
}

sealed class Status {
    class OnDeleted(val info: ScheduleInformation) : Status()
    class OnUpdated(val info: ScheduleInformation) : Status()
    object OnCancel : Status()
}
