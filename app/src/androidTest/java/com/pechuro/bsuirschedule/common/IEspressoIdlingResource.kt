package com.pechuro.bsuirschedule.common

import androidx.test.espresso.IdlingResource

interface IEspressoIdlingResource : IdlingResource {

    fun acquire()

    fun release()
}