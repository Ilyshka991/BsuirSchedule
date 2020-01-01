package com.pechuro.bsuirschedule.feature.edit.editlesson

import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BaseFragment

class EditLessonFragment : BaseFragment() {

    companion object {

        fun newInstance() = EditLessonFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_edit_lesson
}