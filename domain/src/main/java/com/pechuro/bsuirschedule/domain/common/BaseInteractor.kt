package com.pechuro.bsuirschedule.domain.common

import kotlinx.coroutines.Deferred

abstract class BaseInteractor<out Type, in Params> where Type : Any {

    protected abstract suspend fun runAsync(params: Params): Deferred<Type>

    suspend fun executeAsync(params: Params): Deferred<Type> = runAsync(params)

    object NoParams
}