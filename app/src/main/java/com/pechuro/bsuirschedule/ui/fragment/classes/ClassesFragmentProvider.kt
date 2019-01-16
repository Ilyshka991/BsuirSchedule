package com.pechuro.bsuirschedule.ui.fragment.classes

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ClassesFragmentModule::class, ClassesItemFragmentProvider::class])
    fun bind(): ClassesFragment
}
