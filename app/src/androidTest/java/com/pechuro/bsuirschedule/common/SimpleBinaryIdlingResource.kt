package com.pechuro.bsuirschedule.common

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean


class SimpleBinaryIdlingResource(private val resourceName: String) : IEspressoIdlingResource {

    private val isIdle = AtomicBoolean(true)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = isIdle.get()

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    override fun acquire() {
        setIdle(false)
    }

    override fun release() {
        setIdle(true)
    }

    private fun setIdle(isIdleNow: Boolean) {
        if (isIdleNow == isIdle.get()) return
        isIdle.set(isIdleNow)
        if (isIdleNow) {
            resourceCallback?.onTransitionToIdle()
        }
    }
}