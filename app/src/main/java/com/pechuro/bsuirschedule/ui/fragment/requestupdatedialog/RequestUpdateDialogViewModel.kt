package com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog

import androidx.databinding.ObservableBoolean
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
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
        repository.update(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    status.call(Status.OnUpdated(info))
                }, {
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
}