package com.pechuro.bsuirschedule.ui.activity.navigation

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

sealed class FabCommunicationEvent

object OnFabClick : FabCommunicationEvent()
object OnFabShow : FabCommunicationEvent()
object OnFabHide : FabCommunicationEvent()
object OnFabShowPos : FabCommunicationEvent()

object FabCommunication {
    private val publisher = PublishSubject.create<FabCommunicationEvent>()

    fun publish(event: FabCommunicationEvent) {
        publisher.onNext(event)
    }

    fun <T : FabCommunicationEvent> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}
