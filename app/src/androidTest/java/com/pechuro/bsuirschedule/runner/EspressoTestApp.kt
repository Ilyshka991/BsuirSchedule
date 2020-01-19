package com.pechuro.bsuirschedule.runner

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.IEspressoIdlingResource
import com.pechuro.bsuirschedule.common.SimpleBinaryIdlingResource
import com.pechuro.bsuirschedule.common.SimpleCountingIdlingResource

class EspressoTestApp : App() {

    val countingIdlingResource: IEspressoIdlingResource by lazy {
        SimpleCountingIdlingResource("Global counting")
    }

    val binaryIdlingResource: IEspressoIdlingResource by lazy {
        SimpleBinaryIdlingResource("Global binary")
    }
}

