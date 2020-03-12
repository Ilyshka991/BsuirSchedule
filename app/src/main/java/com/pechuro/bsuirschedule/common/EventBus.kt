package com.pechuro.bsuirschedule.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object EventBus {

    val bus: BroadcastChannel<BaseEvent> = BroadcastChannel(Channel.BUFFERED)

    fun send(event: BaseEvent) {
        bus.offer(event)
    }

    inline fun <reified T : BaseEvent> receive(scope: CoroutineScope, crossinline onEvent: (T) -> Unit) = bus
            .openSubscription()
            .consumeAsFlow()
            .filterIsInstance<T>()
            .onEach { onEvent(it) }
            .launchIn(scope)
}

abstract class BaseEvent