package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.di.module.*
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    ApplicationModule::class,
    AppActivitiesModule::class,
    AppViewModelsModule::class,
    NetworkModule::class,
    SharedPreferencesModule::class,
    DatabaseModule::class,
    InteractorModule::class,
    RepositoryModule::class
])
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}