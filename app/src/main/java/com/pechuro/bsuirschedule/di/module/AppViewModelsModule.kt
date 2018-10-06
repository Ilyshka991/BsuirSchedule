package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivityViewModel
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.ScheduleFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.list.ListViewModel
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentViewModel
import com.pechuro.bsuirschedule.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModelsModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NavigationActivityViewModel::class)
    abstract fun navigationActivity(viewModel: NavigationActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleFragmentViewModel::class)
    abstract fun scheduleFragment(viewModel: ScheduleFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun listFragment(viewModel: ListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartFragmentViewModel::class)
    abstract fun startFragment(viewModel: StartFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDialogFragmentViewModel::class)
    abstract fun testFragment(viewModel: AddDialogFragmentViewModel): ViewModel
}