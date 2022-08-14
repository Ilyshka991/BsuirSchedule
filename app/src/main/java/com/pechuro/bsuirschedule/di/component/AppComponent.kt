package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.di.module.ApplicationModule
import com.pechuro.bsuirschedule.di.module.DataModule
import com.pechuro.bsuirschedule.di.module.DatabaseModule
import com.pechuro.bsuirschedule.di.module.NetworkModule
import com.pechuro.bsuirschedule.di.module.RecyclerViewModule
import com.pechuro.bsuirschedule.di.module.RepositoryModule
import com.pechuro.bsuirschedule.di.module.ViewModelModule
import com.pechuro.bsuirschedule.di.module.WorkerModule
import com.pechuro.bsuirschedule.feature.appwidget.ScheduleWidgetProvider
import com.pechuro.bsuirschedule.feature.appwidget.ScheduleWidgetViewService
import com.pechuro.bsuirschedule.feature.display.fragment.DisplayScheduleFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        ApplicationModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        WorkerModule::class,
        RecyclerViewModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)

    fun inject(dialog: BaseBottomSheetDialog)

    fun inject(displayScheduleFragment: DisplayScheduleFragment)

    fun inject(scheduleWidgetProvider: ScheduleWidgetProvider)

    fun inject(scheduleWidgetViewService: ScheduleWidgetViewService)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}