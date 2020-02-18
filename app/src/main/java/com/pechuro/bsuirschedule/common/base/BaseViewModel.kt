package com.pechuro.bsuirschedule.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected inline fun launchCoroutine(
            context: CoroutineContext = EmptyCoroutineContext,
            crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context) {
        body()
    }

    protected inline fun <T> async(
            context: CoroutineContext = EmptyCoroutineContext,
            crossinline body: suspend CoroutineScope.() -> T
    ) = viewModelScope.async(context) {
        body()
    }

    protected inline fun <T> liveDataFlow(crossinline block: suspend () -> Flow<T>?): LiveData<T> = flow {
        block()?.let { emitAll(it) }
    }.asLiveData()
}
