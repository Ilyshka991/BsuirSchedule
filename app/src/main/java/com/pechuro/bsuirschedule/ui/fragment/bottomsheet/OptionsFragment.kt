package com.pechuro.bsuirschedule.ui.fragment.bottomsheet

import android.content.Context
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
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class OptionsFragment : BottomSheetDialogFragment() {
    companion object {
        private const val ARG_SCHEDULE_TYPE = "schedule_type"

        fun newInstance(scheduleType: Int): OptionsFragment =
                OptionsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SCHEDULE_TYPE, scheduleType)
                    }
                }
    }

    private var callback: ScheduleOptionsCallback? = null

    private lateinit var mViewTypeButton: Button
    private lateinit var mAddButton: Button
    private lateinit var mSubgroupNumberSpinner: Spinner

    @Inject
    lateinit var sharedPref: RxSharedPreferences

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? ScheduleOptionsCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    private fun performDI() = AndroidSupportInjection.inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        mAddButton = view.findViewById(R.id.bar_options_add)
        mSubgroupNumberSpinner = view.findViewById(R.id.bar_options_subgroup_number)
        mViewTypeButton = view.findViewById(R.id.bar_options_view_type)

        setupView()
        setListeners()
        return view
    }

    private fun setupView() {
        if (arguments?.getInt(ARG_SCHEDULE_TYPE) == ScheduleTypes.EMPLOYEE_CLASSES) {
            mSubgroupNumberSpinner.visibility = View.GONE
        }

        when (sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).get()) {
            VIEW_TYPE_DAY -> mViewTypeButton.text = getString(R.string.show_by_days)
            VIEW_TYPE_WEEK -> mViewTypeButton.text = getString(R.string.show_by_weeks)
        }

        mSubgroupNumberSpinner.setSelection(sharedPref.getInteger(SUBGROUP_NUMBER, SUBGROUP_ALL).get())
    }

    private fun setListeners() {
        mSubgroupNumberSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        sharedPref.getInteger(SUBGROUP_NUMBER).set(position)
                    }
                }
        mAddButton.setOnClickListener {
            callback?.addLesson()
            dismiss()
        }
        mViewTypeButton.setOnClickListener {
            sharedPref.getInteger(VIEW_TYPE, VIEW_TYPE_DAY).apply {
                when (get()) {
                    VIEW_TYPE_DAY -> {
                        set(VIEW_TYPE_WEEK)
                        mViewTypeButton.text = getString(R.string.show_by_weeks)

                    }
                    VIEW_TYPE_WEEK -> {
                        set(VIEW_TYPE_DAY)
                        mViewTypeButton.text = getString(R.string.show_by_days)
                    }
                }
            }
        }
    }

    interface ScheduleOptionsCallback {
        fun addLesson()
    }
}
