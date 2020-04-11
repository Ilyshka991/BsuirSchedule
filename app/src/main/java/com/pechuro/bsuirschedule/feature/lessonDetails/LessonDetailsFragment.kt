package com.pechuro.bsuirschedule.feature.lessonDetails

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment

class LessonDetailsFragment : BaseFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(LessonDetailsViewModel::class)
    }

    override val layoutId: Int get() = R.layout.fragment_lesson_details
}
