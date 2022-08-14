package com.pechuro.bsuirschedule.feature.loadInfo

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel.Status.Complete
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel.Status.Error
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel.Status.Loading
import javax.inject.Inject

open class LoadInfoViewModel @Inject constructor(
    private val loadInfo: LoadInfo
) : BaseViewModel() {

    val status = MutableLiveData<Status>()

    init {
        loadInfo()
    }

    fun loadInfo() {
        if (status.value == Loading) return
        launchCoroutine {
            status.value = Loading
            loadInfo.execute(BaseInteractor.NoParams).fold(
                onSuccess = {
                    status.value = Complete
                },
                onFailure = {
                    status.value = Error(it)
                }
            )
        }
    }

    sealed class Status {
        object Loading : Status()
        object Complete : Status()
        data class Error(val exception: Throwable) : Status()
    }
}

