package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.factory.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.MainViewModel
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationViewModel
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel
import com.pechuro.bsuirschedule.feature.flow.FlowViewModel
import com.pechuro.bsuirschedule.feature.itemoptions.ScheduleItemOptionsViewModel
import com.pechuro.bsuirschedule.feature.lessondetails.LessonDetailsViewModel
import com.pechuro.bsuirschedule.feature.loadInfo.LoadInfoViewModel
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemViewModel
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetViewModel
import com.pechuro.bsuirschedule.feature.scheduleoptions.DisplayScheduleOptionsViewModel
import com.pechuro.bsuirschedule.feature.settings.SettingsViewModel
import com.pechuro.bsuirschedule.feature.stafflist.StaffListViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(ModifyScheduleItemViewModel::class)
    fun modifyScheduleItem(viewModel: ModifyScheduleItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StaffListViewModel::class)
    fun staffList(viewModel: StaffListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun settings(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppWidgetConfigurationViewModel::class)
    fun configureScheduleWidget(viewModel: AppWidgetConfigurationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun main(viewModel: MainViewModel): ViewModel
}