package com.pechuro.bsuirschedule.feature.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.Window
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.ext.setHeight
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.fragment_navigation_sheet.*
import kotlin.math.max
import kotlin.math.roundToInt

class NavigationSheet : BaseBottomSheetDialog() {

    companion object {
        private const val PEEK_HEIGHT_PERCENT = 0.4
        private const val ACTION_VIBRATION_DURATION_MS = 2L
    }

    override val layoutId: Int = R.layout.fragment_navigation_sheet

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationSheetViewModel::class)
    }

    private val vibrator by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val maxHeight by lazy(LazyThreadSafetyMode.NONE) {
        val rootView = activity?.window?.findViewById<View>(Window.ID_ANDROID_CONTENT)
        if (rootView == null) {
            Logger.w("activity is null!")
        }
        rootView?.height ?: 0
    }
    private var peekHeight = 0

    private val actionViewsHeight by lazy(LazyThreadSafetyMode.NONE) {
        navigationSheetParentView.children
                .filter { view -> view != navigationSheetItemRecyclerView }
                .sumBy { view -> view.height + view.marginTop + view.marginBottom }
    }
    private val initialRecyclerViewHeight by lazy(LazyThreadSafetyMode.NONE) {
        peekHeight = (maxHeight * PEEK_HEIGHT_PERCENT).roundToInt()
        val minHeight = resources.getDimensionPixelOffset(R.dimen.navigation_sheet_min_recyclerview_height)
        val otherViewsHeight = actionViewsHeight +
                navigationSheetItemRecyclerView.marginTop +
                navigationSheetItemRecyclerView.marginBottom
        val calculatedHeight = peekHeight - otherViewsHeight
        if (calculatedHeight < minHeight) {
            peekHeight += minHeight - calculatedHeight
        }
        max(minHeight, calculatedHeight)
    }

    private val topBackgroundGradientDrawable by lazy(LazyThreadSafetyMode.NONE) {
        val layerDrawable = navigationSheetParentView.background as? LayerDrawable
        val topLayer = layerDrawable?.findDrawableByLayerId(R.id.backgroundNavigationSheetTopLayer) as? GradientDrawable
        if (topLayer == null) Logger.w("Background top layer not found")
        topLayer
    }
    private val initialTopCornersRadius by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.navigation_sheet_top_corner_radius)
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

    private val itemTouchHelper = ItemTouchHelper(
            NavigationItemTouchCallback(
                    onDelete = ::deleteItemAt,
                    onUpdate = ::updateItemAt,
                    onActionReadyToPerform = ::makeActionVibration
            )
    )

    private val sheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (slideOffset >= 0) changeViewsPositions(slideOffset)
        }

        @SuppressLint("SwitchIntDef")
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            //Make sure, that views in correct positions
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> changeViewsPositions(1F)
                BottomSheetBehavior.STATE_COLLAPSED -> changeViewsPositions(0F)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).apply {
        setOnShowListener {
            findViewById<View>(R.id.design_bottom_sheet)?.let {
                it.setHeight(maxHeight)
                BottomSheetBehavior.from(it).apply {
                    navigationSheetItemRecyclerView.setHeight(initialRecyclerViewHeight)
                    peekHeight = this@NavigationSheet.peekHeight
                    addBottomSheetCallback(sheetCallback)
                    sheetCallback.onStateChanged(it, state)
                }
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
            itemTouchHelper.attachToRecyclerView(this)
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
        viewModel.navigationInfoData.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun changeViewsPositions(slideOffset: Float) {
        val availableRecyclerViewHeight = navigationSheetItemRecyclerView.run {
            maxHeight - marginTop - marginBottom - initialRecyclerViewHeight
        }
        val heightOffset = availableRecyclerViewHeight * slideOffset
        val newRecyclerHeight = initialRecyclerViewHeight + heightOffset.roundToInt()
        navigationSheetItemRecyclerView.setHeight(newRecyclerHeight)

        navigationSheetParentView.children
                .filter { view -> view != navigationSheetItemRecyclerView }
                .forEach {
                    it.translationY = actionViewsHeight * slideOffset
                }

        topBackgroundGradientDrawable?.cornerRadius = (1 - slideOffset) * initialTopCornersRadius
    }

    private fun deleteItemAt(position: Int) {
        val scheduleInfo = adapter.getItemAt(position) as? NavigationSheetItemInformation.Content
        if (scheduleInfo != null) {
            viewModel.deleteSchedule(scheduleInfo.schedule)
        } else {
            Logger.w("Try to delete item, which is not a subtype of NavigationSheetItemInformation.Content")
        }
    }

    private fun updateItemAt(position: Int) {
        resetSwipedItems()
        val scheduleInfo = adapter.getItemAt(position) as? NavigationSheetItemInformation.Content
        if (scheduleInfo != null) {
            viewModel.updateSchedule(scheduleInfo.schedule)
        } else {
            Logger.w("Try to update item, which is not a subtype of NavigationSheetItemInformation.Content")
        }
    }

    private fun resetSwipedItems() {
        itemTouchHelper.attachToRecyclerView(null)
        itemTouchHelper.attachToRecyclerView(navigationSheetItemRecyclerView)
    }

    private fun makeActionVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ACTION_VIBRATION_DURATION_MS, VibrationEffect.EFFECT_TICK))
        } else {
            vibrator.vibrate(ACTION_VIBRATION_DURATION_MS)
        }
    }
}