package com.pechuro.bsuirschedule.ui.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_ALL
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.SUBGROUP_NUMBER
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.constants.SharedPrefConstants.VIEW_TYPE_WEEK
import com.pechuro.bsuirschedule.ui.utils.EventBus
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class BottomOptionsFragment : BottomSheetDialogFragment() {
    private lateinit var _viewTypeButton: Button
    private lateinit var _addButton: Button
    private lateinit var _subgroupNumberSpinner: Spinner

    @Inject
    lateinit var sharedPref: RxSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    private fun performDI() = AndroidSupportInjection.inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        _addButton = view.findViewById(R.id.bar_options_add)
        _subgroupNumberSpinner = view.findViewById(R.id.bar_options_subgroup_number)
        _viewTypeButton = view.findViewById(R.id.bar_options_view_type)

        setupView()
        setListeners()
        return view
    }

    private fun setupView() {
        if (arguments?.getInt(ARG_SCHEDULE_TYPE) == ScheduleTypes.EMPLOYEE_CLASSES) {
            _subgroupNumberSpinner.visibility = View.GONE
        }

        when (sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).get()) {
            VIEW_TYPE_DAY -> _viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_days)
            VIEW_TYPE_WEEK -> _viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_weeks)
        }

        _subgroupNumberSpinner.setSelection(sharedPref.getInteger(SUBGROUP_NUMBER, SUBGROUP_ALL).get())
    }

    private fun setListeners() {
        _subgroupNumberSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        sharedPref.getInteger(SUBGROUP_NUMBER).set(position)
                    }
                }
        _addButton.setOnClickListener {
            EventBus.publish(BottomOptionsEvent.OnAddLesson)
            dismiss()
        }
        _viewTypeButton.setOnClickListener {
            sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).apply {
                when (get()) {
                    VIEW_TYPE_DAY -> {
                        set(VIEW_TYPE_WEEK)
                        _viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_weeks)

                    }
                    VIEW_TYPE_WEEK -> {
                        set(VIEW_TYPE_DAY)
                        _viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_days)
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_SCHEDULE_TYPE = "schedule_type"

        fun newInstance(scheduleType: Int): BottomOptionsFragment =
                BottomOptionsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SCHEDULE_TYPE, scheduleType)
                    }
                }
    }
}
