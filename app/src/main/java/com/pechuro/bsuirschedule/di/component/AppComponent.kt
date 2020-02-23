package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.di.module.*
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    ApplicationModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    WorkerModule::class
])
interface AppComponent {

    fun inject(app: App)

    fun inject(fragment: BaseFragment)

    fun inject(dialog: BaseBottomSheetDialog)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}