package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class,
    ApplicationModule::class,
    DatabaseModule::class,
    AppActivitiesModule::class,
    AppFragmentsModule::class])
interface AppComponent {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(app: App): Builder

        fun build(): AppComponent
    }
}