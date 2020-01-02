package com.pechuro.bsuirschedule.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
}
