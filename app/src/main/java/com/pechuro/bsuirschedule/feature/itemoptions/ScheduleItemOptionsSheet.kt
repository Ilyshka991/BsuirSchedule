package com.pechuro.bsuirschedule.feature.itemoptions

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.getCallbackOrNull
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import kotlinx.android.synthetic.main.sheet_schedule_item_options.*

class ScheduleItemOptionsSheet : BaseBottomSheetDialog() {

    interface ActionCallback {

        fun onScheduleItemOptionsEditClicked(items: List<ScheduleItem>)
    }

    companion object {

        const val TAG = "ScheduleItemOptionsSheet"

        private const val BUNDLE_ITEM = "BUNDLE_ITEM"

        fun newInstance(item: DisplayScheduleItem) = ScheduleItemOptionsSheet().apply {
            arguments = bundleOf(BUNDLE_ITEM to item)
        }
    }

    private var actionCallback: ActionCallback? = null

    override val layoutId = R.layout.sheet_schedule_item_options

    private val displayItem: DisplayScheduleItem by args(BUNDLE_ITEM)

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ScheduleItemOptionsViewModel::class)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
        displayItem.scheduleItem?.let {
            AppAnalytics.report(AppAnalyticsEvent.DisplaySchedule.ItemOptionOpened(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    private fun initView() {
        scheduleItemOptionsTitle.text = getString(
            R.string.schedule_item_options_sheet_title,
            displayItem.scheduleItem?.subject
        )
        scheduleItemOptionsEditButton.setSafeClickListener {
            actionCallback?.onScheduleItemOptionsEditClicked(getScheduleItems())
        }
        scheduleItemOptionsDeleteButton.setSafeClickListener {
            displayItem.scheduleItem?.let {
                AppAnalytics.report(AppAnalyticsEvent.DisplaySchedule.ItemDeleted(it))
            }
            viewModel.deleteScheduleItem(getScheduleItems())
            dismiss()
        }
    }

    private fun getScheduleItems() = when (val item = displayItem) {
        is DisplayScheduleItem.WeekClasses -> item.allScheduleItems
        else -> listOfNotNull(item.scheduleItem)
    }
}