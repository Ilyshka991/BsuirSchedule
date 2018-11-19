package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constants.SharedPrefConstants
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.adddialog.OnAddDialogDismissEvent
import com.pechuro.bsuirschedule.ui.fragment.adddialog.SetDialogCancelable
import com.pechuro.bsuirschedule.ui.utils.EventBus
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class AddFragment : BaseFragment<FragmentAddBinding, AddFragmentViewModel>() {
    @Inject
    lateinit var gson: Gson

    override val viewModel: AddFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddFragmentViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))
    override val layoutId: Int
        get() = R.layout.fragment_add

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
        setEventListeners()
    }

    private fun setEventListeners() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is OnSuccess -> {
                    context?.defaultSharedPreferences?.edit {
                        putString(SharedPrefConstants.SCHEDULE_INFO, gson.toJson(it.info))
                    }

                    viewDataBinding.buttonOk.isEnabled = true
                    Toast.makeText(context, getString(R.string.success), Toast.LENGTH_LONG).show()
                    EventBus.publish(OnAddDialogDismissEvent)
                }

                is OnClearError -> {
                    viewDataBinding.errorTextView.text = ""
                }

                is OnError -> {
                    EventBus.publish(SetDialogCancelable(true))
                    viewDataBinding.buttonOk.isEnabled = true

                    viewDataBinding.errorTextView.text = getString(it.messageId)
                }

                is OnCancel -> {
                    viewDataBinding.errorTextView.text = ""
                    viewDataBinding.buttonOk.isEnabled = true
                    EventBus.publish(SetDialogCancelable(true))
                }

                is OnLoading -> {
                    EventBus.publish(SetDialogCancelable(false))
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

        fun newInstance(scheduleType: Int): AddFragment {
            val args = Bundle()
            args.putInt(ARG_SCHEDULE_TYPE, scheduleType)

            val fragment = AddFragment()
            fragment.arguments = args
            return fragment
        }
    }
}