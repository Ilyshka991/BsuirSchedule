package com.pechuro.bsuirschedule.feature.main.itemoptions

import com.pechuro.bsuirschedule.toDelete.ScheduleRepository
import com.pechuro.bsuirschedule.common.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ItemOptionsDialogViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val status = SingleLiveEvent<Status>()

    fun delete(id: Int) {
        repository.deleteItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    status.call(Status.OnDeleted(id))
                }, {})
                .let(compositeDisposable::add)
    }
}

sealed class Status {
    class OnDeleted(val itemId: Int) : Status()
}