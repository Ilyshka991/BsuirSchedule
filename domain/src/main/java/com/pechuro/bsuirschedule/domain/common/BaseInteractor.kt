package com.pechuro.bsuirschedule.domain.common

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseInteractor<out Type, in Params> where Type : Any {

    protected abstract suspend fun run(params: Params): Type

    suspend fun execute(params: Params): Type = run(params)

    suspend fun executeAsync(params: Params, context: CoroutineContext): Deferred<Type> =
            withContext(context) {
                async { run(params) }
            }


    object NoParams
}