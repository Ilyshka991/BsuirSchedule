package com.pechuro.bsuirschedule.feature.datepicker

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.core.os.bundleOf
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.ext.parcelableOrException
import kotlinx.android.synthetic.main.sheet_display_schedule_date_picker.*
import java.util.*

class DisplayScheduleDatePickerSheet : BaseBottomSheetDialog() {

    companion object {

        const val TAG = "DisplayScheduleDatePickerSheet"

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        fun newInstance(args: DisplayScheduleDatePickerSheetArgs) = DisplayScheduleDatePickerSheet().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    override val layoutId = R.layout.sheet_display_schedule_date_picker

    private val args: DisplayScheduleDatePickerSheetArgs by lazy(LazyThreadSafetyMode.NONE) {
        parcelableOrException<DisplayScheduleDatePickerSheetArgs>(BUNDLE_ARGS)
    }

    private val onDateChangedListener = CalendarView.OnDateChangeListener { _, year, month, day ->
        val selectedDate = Calendar.getInstance().apply {
            set(year, month, day)
        }
        EventBus.send(ScheduleDatePickedEvent(selectedDate.time))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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