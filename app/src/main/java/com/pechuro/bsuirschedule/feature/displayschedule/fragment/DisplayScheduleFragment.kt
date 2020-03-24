package com.pechuro.bsuirschedule.feature.displayschedule.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.feature.displayschedule.DisplayScheduleViewModel
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import kotlinx.android.synthetic.main.fragment_display_schedule.*

class DisplayScheduleFragment : BaseFragment() {

    companion object {
        private const val ARG_ITEM_INFO = "ARG_ITEM_INFO"

        fun newInstance(info: DisplayScheduleItemInfo) = DisplayScheduleFragment().apply {
            arguments = bundleOf(ARG_ITEM_INFO to info)
        }
    }

    override val layoutId: Int = R.layout.fragment_display_schedule

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleViewModel::class, owner = parentFragment ?: this)
    }
    private val itemInfo: DisplayScheduleItemInfo by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getParcelable<DisplayScheduleItemInfo>(ARG_ITEM_INFO) as DisplayScheduleItemInfo
    }
    private val itemsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DisplayScheduleItemAdapter(viewModel::onScheduleItemClicked).also {
            it.setHasStableIds(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        displayScheduleRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = itemsAdapter.apply {
                setHasStableIds(true)
            }
        }
    }

    private fun observeData() {
        viewModel.getItems(itemInfo).nonNull().observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it)
        }
    }
}