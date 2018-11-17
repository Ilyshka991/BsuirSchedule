package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.splash.SplashActivityViewModel
import com.pechuro.bsuirschedule.ui.base.stubs.StubViewModel
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemViewModel
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamViewModel
import com.pechuro.bsuirschedule.ui.fragment.optiondialog.DrawerOptionsDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentViewModel
import com.pechuro.bsuirschedule.ui.utils.ViewModelFactory
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
    @ViewModelKey(ClassesItemViewModel::class)
    abstract fun listClassesWeekFragment(viewModel: ClassesItemViewModel): ViewModel

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
    @ViewModelKey(StubViewModel::class)
    abstract fun stubViewModel(viewModel: StubViewModel): ViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(DrawerOptionsDialogViewModel::class)
    abstract fun drawerOptionsDialog(viewModel: DrawerOptionsDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditLessonFragmentViewModel::class)
    abstract fun editLessonFragment(viewModel: EditLessonFragmentViewModel): ViewModel
}