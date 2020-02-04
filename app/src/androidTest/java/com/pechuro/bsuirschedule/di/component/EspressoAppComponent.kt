package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.di.module.*
import com.pechuro.bsuirschedule.runner.EspressoTestApp
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    ApplicationModule::class,
    AppViewModelsEspressoModule::class,
    NetworkModule::class,
    SharedPreferencesModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    IdlingResourceModule::class
])
interface EspressoAppComponent : AppComponent {

    fun inject(app: EspressoTestApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): EspressoAppComponent
    }
}