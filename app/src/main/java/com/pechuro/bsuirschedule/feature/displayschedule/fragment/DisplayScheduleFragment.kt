package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleViewModel
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import kotlinx.android.synthetic.main.fragment_display_schedule.*

class DisplayScheduleFragment : BaseFragment() {

    companion object {
        private const val ARG_INFO = "ARG_INFO"

        fun newInstance(info: DisplayScheduleItemInfo) = DisplayScheduleFragment().apply {
            arguments = bundleOf(ARG_INFO to info)
        }
    }

    override val layoutId: Int = R.layout.fragment_display_schedule

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleViewModel::class, owner = parentFragment ?: this)
    }
    private val info: DisplayScheduleItemInfo by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getParcelable<DisplayScheduleItemInfo>(ARG_INFO) as DisplayScheduleItemInfo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayScheduleRecyclerView.text = info.toString()
    }
}