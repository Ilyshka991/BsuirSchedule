package com.pechuro.bsuirschedule.ui.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseEvent

object OnFabClick : BaseEvent()
object OnFabShow : BaseEvent()
object OnFabHide : BaseEvent()
object OnFabShowPos : BaseEvent()

object EventBus {
    private val publisher = PublishSubject.create<BaseEvent>()

    fun publish(event: BaseEvent) {
        publisher.onNext(event)
    }

    fun <T : BaseEvent> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}
