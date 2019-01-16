package com.pechuro.bsuirschedule.ui.fragment.edit

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentEditLessonBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment

class EditLessonFragment : BaseFragment<FragmentEditLessonBinding, EditLessonFragmentViewModel>() {

    override val viewModel: EditLessonFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(EditLessonFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_edit_lesson
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    companion object {
        fun newInstance() = EditLessonFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}