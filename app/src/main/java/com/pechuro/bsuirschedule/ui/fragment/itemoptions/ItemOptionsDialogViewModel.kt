package com.pechuro.bsuirschedule.ui.fragment.itemoptions

import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ItemOptionsDialogViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val command = SingleLiveEvent<ItemOptionsEvent>()

    fun delete(id: Int) {
        compositeDisposable.add(
                repository.delete(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            command.call(OnDeleted(id))
                        }, {})
        )
    }
}