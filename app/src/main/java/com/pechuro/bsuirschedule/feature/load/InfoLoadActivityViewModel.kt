package com.pechuro.bsuirschedule.feature.load

import com.pechuro.bsuirschedule.common.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivityViewModel.Status.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class InfoLoadActivityViewModel @Inject constructor(
        private val loadInfo: LoadInfo
) : BaseViewModel() {

    val status: Observable<Status>
        get() = _status
    private val _status = BehaviorSubject.create<Status>()

    init {
        loadInfo()
    }

    fun loadInfo() {
        loadInfo.execute(BaseInteractor.NoParams)
                .doOnSubscribe {
                    _status.onNext(LOADING)
                }
                .subscribe({
                    _status.onNext(COMPLETE)
                }, {
                    _status.onNext(ERROR)
                })
                .let(compositeDisposable::add)
    }

    enum class Status {
        LOADING, COMPLETE, ERROR
    }
}

