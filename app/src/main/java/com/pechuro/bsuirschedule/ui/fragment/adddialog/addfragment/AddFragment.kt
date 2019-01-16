package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.prefs.PrefsConstants
import com.pechuro.bsuirschedule.data.prefs.PrefsDelegate
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus

class AddFragment : BaseFragment<FragmentAddBinding, AddFragmentViewModel>() {

    override val viewModel: AddFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddFragmentViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))
    override val layoutId: Int
        get() = R.layout.fragment_add

    private var _lastScheduleInfo: ScheduleInformation by PrefsDelegate(PrefsConstants.SCHEDULE_INFO, ScheduleInformation())

    private val scheduleType: Int? by lazy {
        arguments?.getInt(ARG_SCHEDULE_TYPE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && scheduleType != null) {
            viewModel.loadSuggestions(scheduleType!!)
        }
        setListeners()
        subscribeToLiveData()
        setStatusListeners()
    }

    private fun setStatusListeners() {
        viewModel.status.observe(this, Observer {
            when (it) {
                is Status.OnSuccess -> {
                    _lastScheduleInfo = it.info
                    EventBus.publish(AddDialogEvent.OnSuccess)
                }

                is Status.OnClearError -> {
                    viewDataBinding.textErrorValidation.text = ""
                }

                is Status.OnError -> {
                    EventBus.publishWithReplay(AddDialogEvent.OnLoading(true))
                    viewDataBinding.textErrorValidation.text = getString(it.messageId)
                }

                is Status.OnCancel -> {
                    viewDataBinding.textErrorValidation.text = ""
                    viewDataBinding.buttonOk.isEnabled = true
                    EventBus.publishWithReplay(AddDialogEvent.OnLoading(true))
                }

                is Status.OnLoading -> {
                    EventBus.publishWithReplay(AddDialogEvent.OnLoading(false))
                }
            }
        })
    }

    private fun loadSchedule() {
        val scheduleName = viewDataBinding.textInput.text.toString()

        val types = arrayListOf<Int>()
        if (viewDataBinding.typeClasses.isChecked) {
            types.add(if (scheduleType == STUDENT)
                STUDENT_CLASSES else EMPLOYEE_CLASSES)
        }
        if (viewDataBinding.typeExams.isChecked) {
            types.add(if (scheduleType == STUDENT)
                STUDENT_EXAMS else EMPLOYEE_EXAMS)
        }

        viewModel.loadSchedule(scheduleName, types)
    }

    private fun setListeners() {
        viewDataBinding.buttonOk.setOnClickListener {
            loadSchedule()
        }
        viewDataBinding.buttonRetry.setOnClickListener {
            loadSchedule()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.suggestions.observe(this,
                Observer {
                    if (it != null) {
                        val adapter = ArrayAdapter<String>(
                                viewDataBinding.textInput.context,
                                R.layout.item_autocomplete_adapter,
                                it)
                        viewDataBinding.textInput.setAdapter(adapter)
                    }
                })
    }

    companion object {
        private const val ARG_SCHEDULE_TYPE = "arg_add_fragment_schedule_type"

        fun newInstance(scheduleType: Int) = AddFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SCHEDULE_TYPE, scheduleType)
            }
        }
    }
}