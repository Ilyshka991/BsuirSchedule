package com.pechuro.bsuirschedule.ui.utils

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseEvent

object EventBus {
    private val publishSubject = PublishSubject.create<BaseEvent>()
    private val behaviorSubject = BehaviorSubject.create<BaseEvent>()

    fun publish(event: BaseEvent) {
        publishSubject.onNext(event)
    }

    fun publishWithReplay(event: BaseEvent) {
        behaviorSubject.onNext(event)
    }

    fun <T : BaseEvent> listen(eventType: Class<T>): Observable<T> = publishSubject.ofType(eventType)

    fun <T : BaseEvent> listenWithReplay(eventType: Class<T>): Observable<T> = behaviorSubject.ofType(eventType)
}
