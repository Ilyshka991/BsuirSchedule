package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_EXAMS
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog

class AddFragment : BaseFragment<FragmentAddBinding, AddFragmentViewModel>(), AddFragmentNavigator {
    companion object {
        const val ARG_SCHEDULE_TYPE = "arg_add_fragment_schedule_type"

        fun newInstance(scheduleType: Int): AddFragment {
            val args = Bundle()
            args.putInt(ARG_SCHEDULE_TYPE, scheduleType)

            val fragment = AddFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: AddFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add

    private val scheduleType: Int by lazy {
        arguments?.getInt(ARG_SCHEDULE_TYPE) ?: throw IllegalStateException()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.navigator = this
        if (savedInstanceState == null) {
            mViewModel.loadSuggestions(scheduleType)
        }
        setupView()
        subscribeToLiveData()
    }

    override fun onLoading() {
        (parentFragment as? AddDialog)?.isCancelable = false
    }

    override fun onError(messageId: Int) {
        (parentFragment as? AddDialog)?.isCancelable = true
        mViewDataBinding.buttonOk.isEnabled = true

        mViewDataBinding.errorTextView.text = "Error"
    }

    override fun onClearError() {
        mViewDataBinding.errorTextView.text = ""
    }

    override fun onSuccess() {
        mViewDataBinding.buttonOk.isEnabled = true
        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
        (parentFragment as? AddDialog)?.dismiss()
    }

    private fun setupView() {
        fun loadSchedule() {
            mViewDataBinding.buttonOk.isEnabled = false

            val scheduleName = mViewDataBinding.textInput.text.toString()

            val types = arrayListOf<Int>()
            if (mViewDataBinding.typeClasses.isChecked) {
                types.add(if (scheduleType == STUDENT)
                    STUDENT_CLASSES else EMPLOYEE_CLASSES)
            }
            if (mViewDataBinding.typeExams.isChecked) {
                types.add(if (scheduleType == STUDENT)
                    STUDENT_EXAMS else EMPLOYEE_EXAMS)
            }

            mViewModel.loadSchedule(scheduleName, types)
        }

        mViewDataBinding.buttonOk.setOnClickListener {
            loadSchedule()
        }

        mViewDataBinding.retryButton.setOnClickListener {
            loadSchedule()
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.suggestions.observe(this,
                Observer {
                    if (it != null) {
                        val adapter = ArrayAdapter<String>(
                                mViewDataBinding.textInput.context,
                                R.layout.item_autocomplete_adapter,
                                it)
                        mViewDataBinding.textInput.setAdapter(adapter)
                    }
                })
    }
}