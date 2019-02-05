package com.pechuro.bsuirschedule.ui.fragment.details

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface LessonDetailsFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): LessonDetailsFragment
}
