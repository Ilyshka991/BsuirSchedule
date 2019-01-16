package com.pechuro.bsuirschedule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.bsuirschedule.di.annotations.ViewModelKey
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivityViewModel
import com.pechuro.bsuirschedule.ui.activity.splash.SplashActivityViewModel
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemViewModel
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionsDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragmentViewModel
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamViewModel
import com.pechuro.bsuirschedule.ui.fragment.itemoptions.ItemOptionsDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog.RequestUpdateDialogViewModel
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentViewModel
import com.pechuro.bsuirschedule.ui.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AppViewModelsModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NavigationActivityViewModel::class)
    fun navigationActivity(viewModel: NavigationActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClassesFragmentViewModel::class)
    fun scheduleFragment(viewModel: ClassesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClassesItemViewModel::class)
    fun listClassesWeekFragment(viewModel: ClassesItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExamViewModel::class)
    fun examFragment(viewModel: ExamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartFragmentViewModel::class)
    fun startFragment(viewModel: StartFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddFragmentViewModel::class)
    fun addFragment(viewModel: AddFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    fun splashActivity(viewModel: SplashActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoLoadActivityViewModel::class)
    fun infoLoadActivity(viewModel: InfoLoadActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsActivityViewModel::class)
    fun settingsActivity(viewModel: SettingsActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditLessonActivityViewModel::class)
    fun editLessonActivity(viewModel: EditLessonActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DrawerOptionsDialogViewModel::class)
    fun drawerOptionsDialog(viewModel: DrawerOptionsDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemOptionsDialogViewModel::class)
    fun itemOptionsDialog(viewModel: ItemOptionsDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditLessonFragmentViewModel::class)
    fun editLessonFragment(viewModel: EditLessonFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DrawerFragmentViewModel::class)
    fun drawerFragment(viewModel: DrawerFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RequestUpdateDialogViewModel::class)
    fun requestUpdateDialog(viewModel: RequestUpdateDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDialogViewModel::class)
    fun addDialog(viewModel: AddDialogViewModel): ViewModel
}