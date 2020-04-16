package com.pechuro.bsuirschedule.feature.itemoptions

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.sheet_schedule_item_options.*

class ScheduleItemOptionsSheet : BaseBottomSheetDialog() {

    override val layoutId = R.layout.sheet_schedule_item_options

    private val args: ScheduleItemOptionsSheetArgs by navArgs()

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
                args.data.scheduleItem?.subject
        )
        scheduleItemOptionsEditButton.setSafeClickListener {
            EventBus.send(EditScheduleItemEvent(getScheduleItems()))
        }
        scheduleItemOptionsDeleteButton.setSafeClickListener {
            viewModel.deleteScheduleItem(getScheduleItems())
            dismiss()
        }
    }

    private fun getScheduleItems() = when (val data = args.data) {
        is DisplayScheduleItem.WeekClasses -> data.allScheduleItems
        else -> listOfNotNull(data.scheduleItem)
    }
}