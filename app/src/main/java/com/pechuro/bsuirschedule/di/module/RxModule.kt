package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesDiffCallback
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class RxModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun provideClassesDiffCallback() = ClassesDiffCallback()

    @Provides
    @Singleton
    fun provideListAdapter(diffCallback: ClassesDiffCallback) = ClassesAdapter(diffCallback)
}