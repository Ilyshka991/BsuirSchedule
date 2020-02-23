package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.factory.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.flow.FlowViewModel
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetViewModel
import com.pechuro.bsuirschedule.feature.updateschedule.UpdateScheduleSheetViewModel
import com.pechuro.bsuirschedule.feature.view.ViewScheduleViewModel
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
    @ViewModelKey(ViewScheduleViewModel::class)
    fun viewSchedule(viewModel: ViewScheduleViewModel): ViewModel
}