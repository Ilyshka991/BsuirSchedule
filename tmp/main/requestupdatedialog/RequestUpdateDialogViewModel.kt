package com.pechuro.bsuirschedule.feature.main.requestupdatedialog

import androidx.databinding.ObservableBoolean
import com.pechuro.bsuirschedule.toDelete.ScheduleRepository
import com.pechuro.bsuirschedule.common.BaseViewModel
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RequestUpdateDialogViewModel @Inject constructor(
        private val repository: ScheduleRepository) : BaseViewModel() {

    val status = SingleLiveEvent<Status>()
    val isLoading = ObservableBoolean()
    val isError = ObservableBoolean()

    fun update(info: ScheduleInformation) {
        isLoading.set(true)
        isError.set(false)
        status.call(Status.OnLoading)
        repository.update(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    status.call(Status.OnUpdated(info))
                }, {
                    status.call(Status.OnLoadingCancel)
                    isError.set(true)
                    isLoading.set(false)
                })
                .let(compositeDisposable::add)
    }

    fun cancel() = status.call(Status.OnCancel)
}

sealed class Status {
    class OnUpdated(val info: ScheduleInformation) : Status()
    object OnCancel : Status()
    object OnLoading : Status()
    object OnLoadingCancel : Status()
}