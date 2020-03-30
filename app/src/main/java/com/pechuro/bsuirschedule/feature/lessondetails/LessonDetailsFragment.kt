package com.pechuro.bsuirschedule.feature.lessondetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_lesson_details.*

class LessonDetailsFragment : BaseFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(LessonDetailsViewModel::class).also {
            it.setLesson(args.lesson)
        }
    }

    private val args: LessonDetailsFragmentArgs by navArgs()

    override val layoutId: Int get() = R.layout.fragment_lesson_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lessonDetailsName.text = viewModel.subject
        lessonDetailsTime.text = viewModel.time
        Glide.with(this)
                .load(viewModel.employeePhotoUrl)
                .placeholder(R.drawable.employee_placeholder)
                .error(R.drawable.employee_placeholder)
                .circleCrop()
                .into(lessonDetailsPhoto)
        lessonDetailsFullName.text = viewModel.employeeFullName
        lessonDetailsWeeks.text = viewModel.weeks
        lessonDetailsAuditory.text = viewModel.auditory
    }
}
