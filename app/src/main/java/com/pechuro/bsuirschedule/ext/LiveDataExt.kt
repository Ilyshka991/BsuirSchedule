package com.pechuro.bsuirschedule.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

internal class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

internal fun <T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}

internal inline fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (t: T) -> Unit) {
    this.observe(owner, Observer { observer(it) })
}

internal inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (t: T?) -> Unit) {
    this.observe(owner, Observer { observer(it) })
}