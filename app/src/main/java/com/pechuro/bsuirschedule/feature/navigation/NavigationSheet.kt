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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bsuir.pechuro.bsuirschedule.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.clearAdapter
import com.pechuro.bsuirschedule.ext.getCallbackOrNull
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.setHeight
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.ext.whenStateAtLeast
import kotlinx.android.synthetic.main.sheet_navigation.*
import kotlin.math.max
import kotlin.math.roundToInt

class NavigationSheet : BaseBottomSheetDialog() {

    interface ActionCallback {

        fun onNavigationSettingsClicked()

        fun onNavigationAddScheduleClicked()

        fun onNavigationScheduleSelected(schedule: Schedule)

        fun onNavigationScheduleDeleted(schedule: Schedule)
    }

    companion object {

        const val TAG = "NavigationSheet"

        private const val PEEK_HEIGHT_PERCENT = 0.4
        private const val ACTION_VIBRATION_DURATION_MS = 2L

        fun newInstance() = NavigationSheet()
    }

    override val layoutId: Int = R.layout.sheet_navigation

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(NavigationSheetViewModel::class, owner = requireActivity())
    }

    private var actionCallback: ActionCallback? = null

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
            .sumOf { view -> view.height + view.marginTop + view.marginBottom }
    }
    private val initialRecyclerViewHeight by lazy(LazyThreadSafetyMode.NONE) {
        peekHeight = (maxHeight * PEEK_HEIGHT_PERCENT).roundToInt()
        val minHeight =
            resources.getDimensionPixelOffset(R.dimen.navigation_sheet_min_recyclerview_height)
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
        val topLayer =
            layerDrawable?.findDrawableByLayerId(R.id.backgroundNavigationSheetTopLayer) as? GradientDrawable
        if (topLayer == null) Logger.w("Background top layer not found")
        topLayer
    }
    private val initialTopCornersRadius by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.navigation_sheet_top_corner_radius)
    }

    private val adapterActionCallback = object : NavigationDrawerAdapter.ActionCallback {

        override fun onScheduleClicked(schedule: Schedule) {
            AppAnalytics.report(AppAnalyticsEvent.Navigation.ScheduleOpened(schedule))
            actionCallback?.onNavigationScheduleSelected(schedule)
        }

        override fun onHintDismissed() {
            viewModel.onHintDismissed()
        }
    }
    private val adapter = NavigationDrawerAdapter().apply {
        actionCallback = adapterActionCallback
        setHasStableIds(true)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).apply {
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

    override fun onDestroyView() {
        navigationSheetItemRecyclerView.clearAdapter()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    private fun initView() {
        navigationSheetItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@NavigationSheet.adapter
            itemTouchHelper.attachToRecyclerView(this)
            val animator = itemAnimator as? SimpleItemAnimator
            animator?.apply {
                addDuration = 0L
            }
        }
        navigationSheetSettingButton.setSafeClickListener {
            actionCallback?.onNavigationSettingsClicked()

        }
        navigationSheetAddButton.setSafeClickListener {
            actionCallback?.onNavigationAddScheduleClicked()
        }
    }

    private fun observeData() {
        viewModel.navigationInfoData.nonNull().observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                whenStateAtLeast(Lifecycle.State.CREATED) {
                    navigationSheetItemRecyclerView?.scrollToPosition(0)
                }
            }
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
            AppAnalytics.report(AppAnalyticsEvent.Navigation.ScheduleDeleted(scheduleInfo.schedule))
            actionCallback?.onNavigationScheduleDeleted(scheduleInfo.schedule)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    ACTION_VIBRATION_DURATION_MS,
                    VibrationEffect.EFFECT_TICK
                )
            )
        } else {
            vibrator.vibrate(ACTION_VIBRATION_DURATION_MS)
        }
    }
}