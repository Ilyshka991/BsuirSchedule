package com.pechuro.bsuirschedule.domain.common

abstract class BaseInteractor<out Type, in Params> where Type : Any {

    abstract fun execute(params: Params): Type

    object NoParams
}