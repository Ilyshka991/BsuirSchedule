package com.pechuro.bsuirschedule.feature.lessondetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import kotlinx.android.synthetic.main.fragment_lesson_details.*

class LessonDetailsFragment : BaseFragment() {

    companion object {

        private const val LESSON_ARG = "lesson_arg"

        const val TAG = "LessonDetailsFragment"

        fun newInstance(lesson: Lesson) = LessonDetailsFragment().apply {
            arguments = bundleOf(LESSON_ARG to lesson)
        }
    }

    private val lesson: Lesson by args(LESSON_ARG)

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(LessonDetailsViewModel::class).also {
            it.lesson = lesson
        }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        LessonDetailsAdapter()
    }

    override val layoutId: Int get() = R.layout.fragment_lesson_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun observeData() {
        viewModel.weeks.nonNull().observe(viewLifecycleOwner) {
            adapter.submitList(getLessonList(it))
        }
    }

    private fun getLessonList(weeks: List<WeekNumber>): List<DetailsItem> {
        val list = mutableListOf<DetailsItem>(DetailsItem.LessonHeader(lesson, weeks))
        list += lesson.auditories.map { DetailsItem.LocationItem(it) }
        return list
    }

    private fun initView() {
        lessonDetailsRootRecycler.adapter = adapter
        lessonDetailsRootRecycler.layoutManager = LinearLayoutManager(requireContext())
    }
}
