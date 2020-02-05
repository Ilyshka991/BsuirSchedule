package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.loadinfo.LoadInfoViewModel
import com.pechuro.bsuirschedule.feature.navigation.NavigationViewModel
import com.pechuro.bsuirschedule.feature.navigation.drawer.NavigationDrawerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AppViewModelsModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoadInfoViewModel::class)
    fun infoLoad(viewModel: LoadInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationDrawerViewModel::class)
    fun navigationDrawer(viewModel: NavigationDrawerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddScheduleViewModel::class)
    fun addSchedule(viewModel: AddScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    fun navigation(viewModel: NavigationViewModel): ViewModel
}