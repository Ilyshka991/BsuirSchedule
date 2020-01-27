package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.common.ViewModelFactory
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivityViewModel
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleViewModel
import com.pechuro.bsuirschedule.feature.main.navigationdrawer.NavigationDrawerFragmentViewModel
import com.pechuro.bsuirschedule.feature.splash.SplashActivityViewModel
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
    @ViewModelKey(InfoLoadActivityViewModel::class)
    fun infoLoad(viewModel: EspressoInfoLoadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationDrawerFragmentViewModel::class)
    fun navigationDrawer(viewModel: NavigationDrawerFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    fun splash(viewModel: SplashActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddScheduleViewModel::class)
    fun addSchedule(viewModel: AddScheduleViewModel): ViewModel
}