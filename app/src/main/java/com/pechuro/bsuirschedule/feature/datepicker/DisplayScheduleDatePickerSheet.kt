package com.pechuro.bsuirschedule.feature.datepicker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.core.os.bundleOf
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.getCallbackOrNull
import kotlinx.android.synthetic.main.sheet_display_schedule_date_picker.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS

class DisplayScheduleDatePickerSheet : BaseBottomSheetDialog() {

    interface ActionCallback {

        fun onDatePickerDatePicked(date: Date)
    }

    companion object {

        const val TAG = "DisplayScheduleDatePickerSheet"

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        fun newInstance(args: DisplayScheduleDatePickerSheetArgs) = DisplayScheduleDatePickerSheet().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    private var actionCallback: ActionCallback? = null

    override val layoutId = R.layout.sheet_display_schedule_date_picker

    private val args: DisplayScheduleDatePickerSheetArgs by args<DisplayScheduleDatePickerSheetArgs>(BUNDLE_ARGS)

    private val onDateChangedListener = CalendarView.OnDateChangeListener { _, year, month, day ->
        val selectedDate = Calendar.getInstance().apply {
            set(year, month, day)
        }

        val millisDiff = selectedDate.timeInMillis - Calendar.getInstance().timeInMillis
        val dayDiff = DAYS.convert(millisDiff, TimeUnit.MILLISECONDS)
        AppAnalytics.report(AppAnalyticsEvent.DisplaySchedule.CalendarDateSelected(dayDiff))

        actionCallback?.onDatePickerDatePicked(selectedDate.time)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
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
        displayCalendarSheetDatePicker.apply {
            date = args.currentDate.time
            minDate = args.startDate.time
            maxDate = args.endDate.time
            setOnDateChangeListener(onDateChangedListener)
        }
    }
}