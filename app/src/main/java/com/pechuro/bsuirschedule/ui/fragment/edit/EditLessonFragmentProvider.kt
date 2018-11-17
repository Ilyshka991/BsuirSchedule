package com.pechuro.bsuirschedule.ui.fragment.edit

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface EditLessonFragmentProvider {

    @ContributesAndroidInjector()
    fun bind(): EditLessonFragment
}
