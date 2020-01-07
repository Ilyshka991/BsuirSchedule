package com.pechuro.bsuirschedule.feature.load

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivityViewModel.Status.*
import javax.inject.Inject

class InfoLoadActivityViewModel @Inject constructor(
        private val loadInfo: LoadInfo
) : BaseViewModel() {

    val status = MutableLiveData(IDLE)

    init {
        loadInfo()
    }

    fun loadInfo() {
        launchCoroutine {
            status.value = LOADING
            loadInfo.execute(BaseInteractor.NoParams).fold(
                    onSuccess = {
                        status.value = COMPLETE
                    },
                    onFailure = {
                        it.printStackTrace()
                        status.value = ERROR
                    }
            )
        }
    }

    enum class Status {
        IDLE, LOADING, COMPLETE, ERROR
    }
}

