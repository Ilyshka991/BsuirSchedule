package com.pechuro.bsuirschedule.feature.itemoptions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.ext.parcelableOrException
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.sheet_schedule_item_options.*

class ScheduleItemOptionsSheet : BaseBottomSheetDialog() {

    companion object {

        const val TAG = "ScheduleItemOptionsSheet"

        private const val BUNDLE_ITEM = "BUNDLE_ITEM"

        fun newInstance(item: DisplayScheduleItem) = ScheduleItemOptionsSheet().apply {
            arguments = bundleOf(BUNDLE_ITEM to item)
        }
    }

    override val layoutId = R.layout.sheet_schedule_item_options

    private val displayItem: DisplayScheduleItem by lazy(LazyThreadSafetyMode.NONE) {
        parcelableOrException<DisplayScheduleItem>(BUNDLE_ITEM)
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ScheduleItemOptionsViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        scheduleItemOptionsTitle.text = getString(
                R.string.schedule_item_options_sheet_title,
                displayItem.scheduleItem?.subject
        )
        scheduleItemOptionsEditButton.setSafeClickListener {
            EventBus.send(EditScheduleItemEvent(getScheduleItems()))
        }
        scheduleItemOptionsDeleteButton.setSafeClickListener {
            viewModel.deleteScheduleItem(getScheduleItems())
            dismiss()
        }
    }

    private fun getScheduleItems() = when (val item = displayItem) {
        is DisplayScheduleItem.WeekClasses -> item.allScheduleItems
        else -> listOfNotNull(item.scheduleItem)
    }
}