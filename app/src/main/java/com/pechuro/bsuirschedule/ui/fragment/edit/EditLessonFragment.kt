package com.pechuro.bsuirschedule.ui.fragment.edit

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentEditLessonBinding
import com.pechuro.bsuirschedule.databinding.FragmentStartBinding
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivity
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog

class EditLessonFragment : BaseFragment<FragmentEditLessonBinding, EditLessonFragmentViewModel>() {
    companion object {
        fun newInstance(): EditLessonFragment {
            val args = Bundle()
            val fragment = EditLessonFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: EditLessonFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(EditLessonFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_edit_lesson
}