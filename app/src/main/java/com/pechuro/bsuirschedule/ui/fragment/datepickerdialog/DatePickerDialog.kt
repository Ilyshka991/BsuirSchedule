package com.pechuro.bsuirschedule.ui.fragment.datepickerdialog

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogDatePickerBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.utils.EventBus
import com.pechuro.bsuirschedule.ui.utils.addDays
import java.util.*

class DatePickerDialog : BaseDialog<DialogDatePickerBinding, DatePickerDialogViewModel>() {

    override val viewModel: DatePickerDialogViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(DatePickerDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(BR.dialog to this)
    override val layoutId: Int
        get() = R.layout.dialog_date_picker

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDatePicker()
    }

    fun chooseDate(day: Int, month: Int, year: Int) {
        val dateTag = "${if (day < 10) "0" else ""}$day-${if (month < 10) "0" else ""}$month-$year"
        EventBus.publish(DatePickerEvent.OnDateChoose(dateTag))
        dismiss()
    }

    fun cancel() {
        dismiss()
    }

    private fun setupDatePicker() = viewDataBinding.datePicker.apply {
        val calendar = Calendar.getInstance()
        minDate = calendar.timeInMillis
        calendar.addDays(ClassesFragment.NUMBER_OF_TABS - 1)
        maxDate = calendar.timeInMillis
    }

    companion object {
        const val TAG = "date_picker_dialog"

        fun newInstance() =
                DatePickerDialog().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}

