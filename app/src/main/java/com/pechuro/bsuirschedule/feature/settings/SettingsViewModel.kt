package com.pechuro.bsuirschedule.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.LiveEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.common.provider.AppUriProvider
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.domain.interactor.GetAppTheme
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.domain.interactor.SetAppTheme
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        val appUriProvider: AppUriProvider,
        private val loadInfo: LoadInfo,
        private val getAppTheme: GetAppTheme,
        private val setAppTheme: SetAppTheme
) : BaseViewModel() {

    private val _stateData = MutableLiveData<State>(State.Idle)
    val stateData: LiveData<State>
        get() = _stateData
    val themeChangedEvent = LiveEvent<Unit>()

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

    fun getCurrentAppTheme(): AppTheme = runBlocking {
        getAppTheme.execute(BaseInteractor.NoParams).getOrDefault(AppTheme.DEFAULT)
    }

    fun setAppTheme(theme: AppTheme) {
        launchCoroutine {
            setAppTheme.execute(SetAppTheme.Params(theme)).onSuccess {
                themeChangedEvent.value = Unit
            }
        }
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
    }
}