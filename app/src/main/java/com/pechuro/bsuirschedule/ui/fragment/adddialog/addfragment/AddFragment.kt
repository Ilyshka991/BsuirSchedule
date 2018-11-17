package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constants.SharedPrefConstants
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import org.jetbrains.anko.defaultSharedPreferences

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

    private var callback: AddFragmentCallback? = null
    private val scheduleType: Int? by lazy {
        arguments?.getInt(ARG_SCHEDULE_TYPE)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = parentFragment as? AddFragmentCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.setNavigator(this)
        if (savedInstanceState == null && scheduleType != null) {
            mViewModel.loadSuggestions(scheduleType!!)
        }
        setListeners()
        subscribeToLiveData()
    }

    override fun onLoading() {
        callback?.setDialogCancelable(false)
    }

    override fun onCancel() {
        mViewDataBinding.errorTextView.text = ""
        mViewDataBinding.buttonOk.isEnabled = true
        callback?.setDialogCancelable(true)
    }

    override fun onError(messageId: Int) {
        callback?.setDialogCancelable(true)
        mViewDataBinding.buttonOk.isEnabled = true

        mViewDataBinding.errorTextView.text = getString(messageId)
    }

    override fun onClearError() {
        mViewDataBinding.errorTextView.text = ""
    }

    override fun onSuccess(scheduleName: String, scheduleType: Int) {
        context?.defaultSharedPreferences?.edit {
            putString(SharedPrefConstants.SCHEDULE_NAME, scheduleName)
            putInt(SharedPrefConstants.SCHEDULE_TYPE, scheduleType)
        }

        mViewDataBinding.buttonOk.isEnabled = true
        Toast.makeText(context, getString(R.string.success), Toast.LENGTH_LONG).show()
        callback?.onDismiss()
    }

    private fun loadSchedule() {
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

    private fun setListeners() {
        mViewDataBinding.buttonOk.setOnClickListener {
            loadSchedule()
        }

        mViewDataBinding.buttonRetry.setOnClickListener {
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

    interface AddFragmentCallback {
        fun onDismiss()

        fun setDialogCancelable(isCancelable: Boolean)
    }
}