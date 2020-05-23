package com.pechuro.bsuirschedule.feature.itemdetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.nonNull
import kotlinx.android.synthetic.main.fragment_schedule_item_details.*

class ScheduleItemDetailsFragment : BaseFragment() {

    companion object {

        private const val ARG_SCHEDULE_ITEM = "ARG_SCHEDULE_ITEM"

        const val TAG = "LessonDetailsFragment"

        fun newInstance(scheduleItem: ScheduleItem) = ScheduleItemDetailsFragment().apply {
            arguments = bundleOf(ARG_SCHEDULE_ITEM to scheduleItem)
        }
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ScheduleItemDetailsViewModel::class)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ScheduleItemDetailsAdapter()
    }

    override val layoutId: Int = R.layout.fragment_schedule_item_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scheduleItem: ScheduleItem by args(ARG_SCHEDULE_ITEM)
        viewModel.init(scheduleItem)
        initView(scheduleItem)
        observeData()
    }

    private fun initView(scheduleItem: ScheduleItem) {
        scheduleItemDetailsToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        scheduleItemDetailsRootRecycler.adapter = adapter
        scheduleItemDetailsTitle.text = scheduleItem.subject
    }

    private fun observeData() {
        viewModel.detailsData.nonNull().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
