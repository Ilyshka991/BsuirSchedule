package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.di.module.AppViewModelsEspressoModule
import com.pechuro.bsuirschedule.di.module.ApplicationModule
import com.pechuro.bsuirschedule.di.module.DatabaseModule
import com.pechuro.bsuirschedule.di.module.IdlingResourceModule
import com.pechuro.bsuirschedule.di.module.NetworkModule
import com.pechuro.bsuirschedule.di.module.RecyclerViewModule
import com.pechuro.bsuirschedule.di.module.RepositoryModule
import com.pechuro.bsuirschedule.di.module.WorkerModule
import com.pechuro.bsuirschedule.runner.EspressoTestApp
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    ApplicationModule::class,
    AppViewModelsEspressoModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    IdlingResourceModule::class,
    WorkerModule::class,
    RecyclerViewModule::class
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