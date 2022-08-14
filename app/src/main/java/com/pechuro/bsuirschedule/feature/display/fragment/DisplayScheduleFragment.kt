package com.pechuro.bsuirschedule.feature.display.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.app
import com.pechuro.bsuirschedule.ext.clearAdapter
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.whenStateAtLeast
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel.Event.OnScheduleItemClicked
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleViewModel.Event.OnScheduleItemLongClicked
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItemInfo
import kotlinx.android.synthetic.main.fragment_display_schedule.*
import javax.inject.Inject

class DisplayScheduleFragment : BaseFragment() {

    companion object {
        private const val ARG_ITEM_INFO = "ARG_ITEM_INFO"

        fun newInstance(info: DisplayScheduleItemInfo) = DisplayScheduleFragment().apply {
            arguments = bundleOf(ARG_ITEM_INFO to info)
        }
    }

    override val layoutId: Int = R.layout.fragment_display_schedule

    @Inject
    protected lateinit var sharedRecycledViewPool: RecyclerView.RecycledViewPool

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(DisplayScheduleViewModel::class, owner = parentFragment ?: this)
    }
    private val itemInfo: DisplayScheduleItemInfo by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getParcelable<DisplayScheduleItemInfo>(ARG_ITEM_INFO) as DisplayScheduleItemInfo
    }
    private val itemsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DisplayScheduleItemAdapter(
            onClickCallback = {
                val scheduleItem = it.scheduleItem ?: return@DisplayScheduleItemAdapter
                viewModel.eventsData.value = OnScheduleItemClicked(scheduleItem)
            },
            onLongClickCallback = {
                viewModel.eventsData.value = OnScheduleItemLongClicked(it)
            }
        ).also {
            it.setHasStableIds(true)
        }
    }

    override fun onAttach(context: Context) {
        context.app.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onDestroyView() {
        displayScheduleRecyclerView.setRecycledViewPool(null)
        displayScheduleRecyclerView.clearAdapter()
        super.onDestroyView()
    }

    private fun initView() {
        displayScheduleRecyclerView.apply {
            setRecycledViewPool(sharedRecycledViewPool)
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false).apply {
                recycleChildrenOnDetach = true
            }
            this.layoutManager = layoutManager
            adapter = itemsAdapter
            val animator = itemAnimator as? SimpleItemAnimator
            animator?.apply {
                addDuration = 0L
            }
        }
    }

    private fun observeData() {
        viewModel.getItems(itemInfo).nonNull().observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it) {
                whenStateAtLeast(Lifecycle.State.CREATED) {
                    if (itemsAdapter.dataUpdatesCount <= 1) {
                        val scrollTo = viewModel.calculateFirstVisibleItem(it)
                        displayScheduleRecyclerView.scrollToPosition(scrollTo)
                    }
                }
            }
        }
    }
}