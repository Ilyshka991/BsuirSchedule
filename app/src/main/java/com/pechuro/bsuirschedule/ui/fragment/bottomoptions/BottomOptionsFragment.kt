package com.pechuro.bsuirschedule.ui.fragment.bottomoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.SUBGROUP_ALL
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.SUBGROUP_NUMBER
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE_DAY
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants.VIEW_TYPE_WEEK
import com.pechuro.bsuirschedule.data.prefs.PrefsDelegate
import com.pechuro.bsuirschedule.ui.utils.EventBus
import dagger.android.support.AndroidSupportInjection


class BottomOptionsFragment : BottomSheetDialogFragment() {
    private lateinit var viewTypeButton: Button
    private lateinit var addButton: Button
    private lateinit var subgroupNumberSpinner: Spinner

    private var _viewType: Int by PrefsDelegate(VIEW_TYPE, VIEW_TYPE_DAY)
    private var _subgroupNumber: Int by PrefsDelegate(SUBGROUP_NUMBER, SUBGROUP_ALL)

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        addButton = view.findViewById(R.id.bar_options_add)
        subgroupNumberSpinner = view.findViewById(R.id.bar_options_subgroup_number)
        viewTypeButton = view.findViewById(R.id.bar_options_view_type)

        setupView()
        setListeners()
        return view
    }

    private fun performDI() = AndroidSupportInjection.inject(this)

    private fun setupView() {
        if (arguments?.getInt(ARG_SCHEDULE_TYPE) == ScheduleTypes.EMPLOYEE_CLASSES) {
            subgroupNumberSpinner.visibility = View.GONE
        }

        when (_viewType) {
            VIEW_TYPE_DAY -> viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_days)
            VIEW_TYPE_WEEK -> viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_weeks)
        }
        subgroupNumberSpinner.setSelection(_subgroupNumber)
    }

    private fun setListeners() {
        subgroupNumberSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        _subgroupNumber = position
                    }
                }

        addButton.setOnClickListener {
            EventBus.publish(BottomOptionsEvent.OnAddLesson)
            dismiss()
        }

        viewTypeButton.setOnClickListener {
            when (_viewType) {
                VIEW_TYPE_DAY -> {
                    _viewType = VIEW_TYPE_WEEK
                    viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_weeks)

                }
                VIEW_TYPE_WEEK -> {
                    _viewType = VIEW_TYPE_DAY
                    viewTypeButton.text = getString(R.string.bottom_sheet_action_show_by_days)
                }
            }
        }
    }

    companion object {
        const val TAG = "bottom_options_dialog"
        private const val ARG_SCHEDULE_TYPE = "schedule_type"

        fun newInstance(scheduleType: Int) = BottomOptionsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SCHEDULE_TYPE, scheduleType)
            }
        }
    }
}
