package com.pechuro.bsuirschedule.domain.common

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseInteractor<out Type, in Params> {

    protected abstract suspend fun run(params: Params): Type

    suspend fun execute(params: Params): Result<Type> = runCatching {
        withContext(Dispatchers.Default) {
            run(params)
        }
    }.onFailure {
        Logger.e(it)
    }

    suspend fun executeAsync(params: Params, context: CoroutineContext): Deferred<Result<Type>> =
        withContext(context) {
            async {
                runCatching {
                    run(params)
                }.onFailure {
                    Logger.e(it)
                }
            }
        }

    object NoParams
}