package com.pechuro.bsuirschedule.ui.fragment.exam

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ExamFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ExamFragmentModule::class])
    fun provideExamFragmentFactory(): ExamFragment
}
