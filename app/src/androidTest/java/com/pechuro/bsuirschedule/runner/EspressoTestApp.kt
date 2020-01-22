package com.pechuro.bsuirschedule.runner

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.IEspressoIdlingResource
import com.pechuro.bsuirschedule.di.component.DaggerEspressoAppComponent
import javax.inject.Inject

class EspressoTestApp : App() {

    @Inject
    lateinit var idlingResource: IEspressoIdlingResource

    override fun initDI() {
        val appComponent = DaggerEspressoAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }
}