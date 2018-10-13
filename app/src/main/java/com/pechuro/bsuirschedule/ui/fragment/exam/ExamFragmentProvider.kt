package com.pechuro.bsuirschedule.ui.fragment.exam

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ExamFragmentProvider {

    @ContributesAndroidInjector(modules = [ExamFragmentModule::class])
    fun provideExamFragmentFactory(): ExamFragment
}
