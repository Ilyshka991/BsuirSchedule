package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.splash.SplashActivityViewModel
import com.pechuro.bsuirschedule.ui.base.ViewModelFactory
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.ClassesDayViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.ClassesWeekViewModel
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamViewModel
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentViewModel
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
    @ViewModelKey(ClassesFragmentViewModel::class)
    abstract fun scheduleFragment(viewModel: ClassesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClassesDayViewModel::class)
    abstract fun listClassesDayFragment(viewModel: ClassesDayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClassesWeekViewModel::class)
    abstract fun listClassesWeekFragment(viewModel: ClassesWeekViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExamViewModel::class)
    abstract fun examFragment(viewModel: ExamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartFragmentViewModel::class)
    abstract fun startFragment(viewModel: StartFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDialogViewModel::class)
    abstract fun addDialog(viewModel: AddDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddFragmentViewModel::class)
    abstract fun addFragment(viewModel: AddFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    abstract fun splashActivity(viewModel: SplashActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoLoadActivityViewModel::class)
    abstract fun infoLoadActivity(viewModel: InfoLoadActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsActivityViewModel::class)
    abstract fun settingsActivity(viewModel: SettingsActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditLessonActivityViewModel::class)
    abstract fun editLessonActivity(viewModel: EditLessonActivityViewModel): ViewModel
}