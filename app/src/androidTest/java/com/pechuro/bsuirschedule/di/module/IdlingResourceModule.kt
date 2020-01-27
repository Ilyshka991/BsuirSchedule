package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.common.IEspressoIdlingResource
import com.pechuro.bsuirschedule.common.SimpleBinaryIdlingResource
import com.pechuro.bsuirschedule.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class IdlingResourceModule {

    @Provides
    @AppScope
    fun provideIdlingResource(): IEspressoIdlingResource {
        return SimpleBinaryIdlingResource("Global")
    }
}