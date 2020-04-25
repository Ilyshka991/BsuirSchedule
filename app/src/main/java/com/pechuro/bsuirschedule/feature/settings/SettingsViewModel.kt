package com.pechuro.bsuirschedule.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val loadInfo: LoadInfo
) : BaseViewModel() {

    private val _stateData = MutableLiveData<State>(State.Idle)
    val stateData: LiveData<State>
        get() = _stateData

    fun updateInfo() {
        launchCoroutine {
            _stateData.value = State.Loading
            loadInfo.execute(BaseInteractor.NoParams).fold(
                    onSuccess = { _stateData.value = State.Idle },
                    onFailure = { exception ->
                        if (exception is DataSourceException.CancellationException) return@fold
                        _stateData.value = State.Error
                    }
            )
        }
    }

    fun setNormalState(): Boolean {
        val isInNormalState = _stateData.value == State.Idle
        if (!isInNormalState) {
            cancelAllJobs()
            _stateData.value = State.Idle
        }
        return !isInNormalState
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
    }
}