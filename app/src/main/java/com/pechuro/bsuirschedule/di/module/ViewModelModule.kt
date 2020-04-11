package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.factory.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.addSchedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel
import com.pechuro.bsuirschedule.feature.scheduleOptions.DisplayScheduleOptionsViewModel
import com.pechuro.bsuirschedule.feature.flow.FlowViewModel
import com.pechuro.bsuirschedule.feature.lessonDetails.LessonDetailsViewModel
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetViewModel
import com.pechuro.bsuirschedule.feature.itemOptions.ScheduleItemOptionsViewModel
import com.pechuro.bsuirschedule.feature.update.UpdateScheduleSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoadInfoViewModel::class)
    fun infoLoad(viewModel: LoadInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationSheetViewModel::class)
    fun navigation(viewModel: NavigationSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddScheduleViewModel::class)
    fun addSchedule(viewModel: AddScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlowViewModel::class)
    fun flow(viewModel: FlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateScheduleSheetViewModel::class)
    fun updateSchedule(viewModel: UpdateScheduleSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisplayScheduleViewModel::class)
    fun viewSchedule(viewModel: DisplayScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisplayScheduleOptionsViewModel::class)
    fun displayOptions(viewModel: DisplayScheduleOptionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LessonDetailsViewModel::class)
    fun lessonDetails(viewModel: LessonDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleItemOptionsViewModel::class)
    fun scheduleItemOptions(viewModel: ScheduleItemOptionsViewModel): ViewModel
}