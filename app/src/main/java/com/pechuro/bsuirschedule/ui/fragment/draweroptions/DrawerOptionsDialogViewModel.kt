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
    val command = SingleLiveEvent<DrawerOptionsEvent>()
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
                            command.call(OnUpdated(info))
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
                            command.call(OnDeleted(info))
                        }, {})
        )
    }

    fun cancel() = command.call(OnCancel)
}