package com.pechuro.bsuirschedule.ui.fragment.details

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentLessonDetailsBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment

class LessonDetailsFragment : BaseFragment<FragmentLessonDetailsBinding, LessonDetailsFragmentViewModel>() {

    override val viewModel: LessonDetailsFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(LessonDetailsFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_lesson_details
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    companion object {
        fun newInstance() = LessonDetailsFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}