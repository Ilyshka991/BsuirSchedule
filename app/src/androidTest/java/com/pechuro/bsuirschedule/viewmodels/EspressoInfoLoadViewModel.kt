package com.pechuro.bsuirschedule.viewmodels

import androidx.lifecycle.Observer
import com.pechuro.bsuirschedule.common.IEspressoIdlingResource
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel
import javax.inject.Inject

class EspressoInfoLoadViewModel @Inject constructor(
        loadInfo: LoadInfo,
        private val espressoIdlingResource: IEspressoIdlingResource
) : LoadInfoViewModel(loadInfo) {

    private val espressoIdlingResourceObserver = Observer<Status> {
        when (it) {
            Status.COMPLETE -> espressoIdlingResource.release()
            Status.LOADING -> espressoIdlingResource.acquire()
            Status.ERROR -> espressoIdlingResource.release()
            null -> { /* nothing */
            }
        }
    }

    init {
        status.observeForever(espressoIdlingResourceObserver)
    }

    override fun onCleared() {
        super.onCleared()
        status.removeObserver(espressoIdlingResourceObserver)
    }
}
