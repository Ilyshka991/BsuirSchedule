package com.pechuro.bsuirschedule.feature.edit.editlesson

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface EditLessonFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): EditLessonFragment
}
