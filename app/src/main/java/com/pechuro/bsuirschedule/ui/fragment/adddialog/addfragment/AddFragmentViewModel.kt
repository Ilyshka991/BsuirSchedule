package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddFragmentViewModel @Inject constructor(private val repository: ScheduleRepository) : BaseViewModel() {
    val isLoading = ObservableField<Boolean>(false)
    val notAddedSchedules = MutableLiveData<Set<String>>()

    fun loadNotAddedSchedules(scheduleType: Int) {
        compositeDisposable.add(
                repository.getNotAddedGroups(0).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            println(it.toString())
                            notAddedSchedules.value = (it.toSet())

                        }, {}))
    }
}