package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.factory.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.flow.FlowViewModel
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetViewModel
import com.pechuro.bsuirschedule.viewmodels.EspressoInfoLoadViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AppViewModelsEspressoModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoadInfoViewModel::class)
    fun infoLoad(viewModel: EspressoInfoLoadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationSheetViewModel::class)
    fun navigationDrawer(viewModel: NavigationSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddScheduleViewModel::class)
    fun addSchedule(viewModel: AddScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlowViewModel::class)
    fun flow(viewModel: FlowViewModel): ViewModel
}