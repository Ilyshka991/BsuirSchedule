package com.pechuro.bsuirschedule.feature.navigation

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setHeight
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.fragment_navigation_sheet.*
import kotlin.math.roundToInt

class NavigationSheet : BaseBottomSheetDialog() {

    companion object {
        private const val PEEK_HEIGHT_PERCENT = 0.4
    }

    override val layoutId: Int = R.layout.fragment_navigation_sheet

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationSheetViewModel::class)
    }

    private val maxHeight by lazy(LazyThreadSafetyMode.NONE) {
        resources.displayMetrics.heightPixels
    }
    private val initialPeekHeight by lazy(LazyThreadSafetyMode.NONE) {
        (maxHeight * PEEK_HEIGHT_PERCENT).roundToInt()
    }
    private val initialRecyclerViewHeight by lazy(LazyThreadSafetyMode.NONE) {
        val otherViewsHeight = navigationSheetParentView.children
                .filter { view -> view != navigationSheetItemRecyclerView }
                .sumBy { view -> view.height + view.marginTop + view.marginBottom }
                .plus(navigationSheetItemRecyclerView.marginTop + navigationSheetItemRecyclerView.marginBottom)
        initialPeekHeight - otherViewsHeight
    }

    private val adapterActionCallback = object : NavigationDrawerAdapter.ActionCallback {

        override fun onScheduleClicked(schedule: Schedule) {
            EventBus.send(NavigationSheetEvent.OnScheduleClick(schedule))
        }

        override fun onScheduleLongClicked(schedule: Schedule) {
            EventBus.send(NavigationSheetEvent.OnScheduleLongClick(schedule))
        }

        override fun onTitleClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationSheetEvent.OnTitleClick(scheduleType))
        }

        override fun onTitleLongClicked(scheduleType: ScheduleType) {
            EventBus.send(NavigationSheetEvent.OnTitleLongClick(scheduleType))
        }
    }
    private val adapter = NavigationDrawerAdapter().apply {
        actionCallback = adapterActionCallback
    }

    private val sheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (slideOffset > 0) {
                val heightOffset = (maxHeight - initialPeekHeight) * slideOffset
                navigationSheetItemRecyclerView.run {
                    setHeight(initialRecyclerViewHeight + heightOffset.roundToInt())
                }
            }
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).apply {
        setOnShowListener {
            findViewById<View>(R.id.design_bottom_sheet)?.let {
                it.setHeight(maxHeight)
                BottomSheetBehavior.from(it).apply {
                    peekHeight = initialPeekHeight
                    addBottomSheetCallback(sheetCallback)
                }
                navigationSheetItemRecyclerView.setHeight(initialRecyclerViewHeight)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        navigationSheetItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@NavigationSheet.adapter
            itemAnimator = null
        }
        navigationSheetSettingButton.setSafeClickListener {
            EventBus.send(NavigationSheetEvent.OnOpenSettings)

        }
        navigationSheetAddButton.setSafeClickListener {
            EventBus.send(NavigationSheetEvent.OnAddSchedule)
        }
    }

    private fun observeData() {
        viewModel.schedules.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}